package ru.mytelegram.financebot.handler.account

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.entity.AccountEntity
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.AccountRepository
import java.math.BigDecimal
import java.util.regex.Pattern

@Service
class AddAccountHandler(
    private val accountRepository: AccountRepository
): CommandHandler {

    companion object {
        private val pattern = Pattern.compile(":[0-9]+")
    }

    override fun getCommandType(): CommandEnum {
        return CommandEnum.ADD_ACCOUNT
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        if (update.hasCallbackQuery()) {
            val callbackData = update.callbackQuery.data
            if (callbackData.equals(CommandEnum.ADD_ACCOUNT.name)) {
                val message = SendMessage()
                message.chatId = userId.toString()
                message.text = "Для добавления нового счета введите текст следующего формата:\n" +
                        "название счета:сумма (число)"
                return message
            }

        } else {
            val input = update.getMessage().text
            val matcher = pattern.matcher(input)
            val isMatch = matcher.find()


            val chatId: Long = update.getMessage().getChatId()
            val message = SendMessage()
            message.chatId = chatId.toString()

            if (isMatch) {
                val accountName = input.substring(0, input.indexOf(":"))
                val amount = input.substring(input.indexOf(":") + 1)

                val accountEntity = AccountEntity(
                    name = accountName,
                    amount = BigDecimal(amount),
                    userId = userId
                )

                accountRepository.save(accountEntity)

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
                message.text = "Успешно добавлен счет: $accountName с суммой счета: $amount"

            } else {
                message.text = "Введен неверный формат для добавления счета. Повторите попытку командой по добавлению счета"
            }

            return message
        }

        return null
    }
}