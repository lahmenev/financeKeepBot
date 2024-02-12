package ru.mytelegram.financebot.handler.transaction

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.entity.TransactionsEntity
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.TransactionsRepository
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class GetExpensesHandler(
    private val transactionRepository: TransactionsRepository
): CommandHandler {
    override fun getCommandType(): CommandEnum {
        return CommandEnum.GET_EXPENSES
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        val currentDate = OffsetDateTime.now()
        val startMonthDate = currentDate.withDayOfMonth(1).withHour(0).withMinute(0)

        val expenseTransactions = transactionRepository.findTransactionsByDate(userId, startMonthDate, currentDate)
            .filter { it.type.equals(TransactionsEntity.Type.SUBTRACT) }
        val expenseSum = expenseTransactions.map { it.amount }.fold(BigDecimal.ZERO) { acc, e -> acc + e }
        val expenseByCategory = expenseTransactions.groupBy({it.category?.name}, {it.amount})

        val sb = StringBuilder()
        sb.append("Общая сумма трат с начала месяца: $expenseSum рублей.\n ")
        sb.append("Расходы по категориям:\n")

        expenseByCategory.forEach {
            val sumByCategory = it.value.map { it }.fold(BigDecimal.ZERO) { acc, e -> acc + e }
            sb.append("${it.key}: $sumByCategory \n")
        }


        val message = SendMessage()
        message.chatId = userId.toString()
        message.text = sb.toString()

        val markupInline = InlineKeyboardMarkup()
        val rowsInline = mutableListOf<List<InlineKeyboardButton>>()
        val rowInline = mutableListOf<InlineKeyboardButton>()

        val keyboardBtn = InlineKeyboardButton()
        keyboardBtn.text = "Назад в меню"
        keyboardBtn.callbackData = "${CommandEnum.START_COMMAND}"
        rowInline.add(keyboardBtn)

        rowsInline.add(rowInline)
        markupInline.setKeyboard(rowsInline)
        message.replyMarkup = markupInline

        return message
    }
}