package ru.mytelegram.financebot.handler.account

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.AccountRepository

@Service
class AccountListHandler(
    private val accountRepository: AccountRepository
): CommandHandler {
    override fun getCommandType(): CommandEnum {
        return CommandEnum.GET_ACCOUNTS
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        val accounts = accountRepository.findByUserId(userId)

        val message = SendMessage()
        message.chatId = userId.toString()

        message.text = if (!accounts.isNullOrEmpty())
            accounts.map { "${it.name}: ${it.amount}" }.toMutableList().joinToString(separator = "\n")
        else
            "список счетов пуст"

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