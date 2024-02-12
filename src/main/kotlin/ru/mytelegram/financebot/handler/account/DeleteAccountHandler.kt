package ru.mytelegram.financebot.handler.account

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.entity.AccountEntity
import ru.mytelegram.financebot.handler.CallbackStateEnum
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.handler.transaction.SubtractTransactionHandler
import ru.mytelegram.financebot.repository.AccountRepository

@Service
class DeleteAccountHandler(
    private val accountRepository: AccountRepository
): CommandHandler {

    override fun getCommandType(): CommandEnum {
        return CommandEnum.DELETE_ACCOUNT
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.DELETE_ACCOUNT.name)) {
            val message = SendMessage()
            message.chatId = userId.toString()
            message.text = CallbackStateEnum.SELECT_ACCOUNT_CALLBACK.description

            val markupInline = InlineKeyboardMarkup()
            val rowsInline = mutableListOf<List<InlineKeyboardButton>>()
            val rowInline = mutableListOf<InlineKeyboardButton>()

            val accounts = accountRepository.findByUserId(userId)

            if (!accounts.isEmpty()) {
                accounts.map { it.name }.forEach {
                    val keyboardBtn = InlineKeyboardButton()
                    keyboardBtn.text = it
                    keyboardBtn.callbackData = "${CallbackStateEnum.SELECT_ACCOUNT_CALLBACK.name}:$it"
                    rowInline.add(keyboardBtn)
                }
                rowsInline.add(rowInline)
                markupInline.setKeyboard(rowsInline)
                message.replyMarkup = markupInline
            } else {
                val keyboardBtn = InlineKeyboardButton()
                keyboardBtn.text = "Назад в меню"
                keyboardBtn.callbackData = "${CommandEnum.START_COMMAND}"
                rowInline.add(keyboardBtn)

                rowsInline.add(rowInline)
                markupInline.setKeyboard(rowsInline)
                message.replyMarkup = markupInline
                message.text = "Список счетов пуст"
            }

            return message

        } else if (update.hasCallbackQuery() && update.callbackQuery.data.contains(CallbackStateEnum.SELECT_ACCOUNT_CALLBACK.name)) {
            val accountName = update.callbackQuery.data.substring(update.callbackQuery.data.indexOf(":") + 1)
            val chatId: Long = update.callbackQuery.message.chatId

            accountRepository.deleteByNameAndUserId(accountName, userId)

            val message = SendMessage()
            message.chatId = chatId.toString()
            message.text = "Счет $accountName успешно удален"

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

        return null
    }
}