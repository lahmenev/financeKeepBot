package ru.mytelegram.financebot.handler.category

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler

@Service
class CategoryHandler: CommandHandler {
    override fun getCommandType(): CommandEnum {
        return CommandEnum.CATEGORY_COMMAND
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        if (update.hasCallbackQuery()) {
            val callbackData = update.callbackQuery.data

            if (callbackData.equals(CommandEnum.CATEGORY_COMMAND.name)) {
                val message = SendMessage()
                message.chatId = userId.toString()
                message.text = "Выберите операцию"

                val markupInline = InlineKeyboardMarkup()
                val rowsInline = mutableListOf<List<InlineKeyboardButton>>()

                val accountCommands = listOf(
                    CommandEnum.GET_CATEGORIES,
                    CommandEnum.ADD_CATEGORY,
                    CommandEnum.DELETE_CATEGORY,
                    CommandEnum.START_COMMAND,
                )

                accountCommands.forEach {
                    val rowInline = mutableListOf<InlineKeyboardButton>()
                    val keyboardBtn = InlineKeyboardButton()
                    keyboardBtn.text = it.description
                    keyboardBtn.callbackData = it.name
                    rowInline.add(keyboardBtn)
                    rowsInline.add(rowInline)
                }

                markupInline.setKeyboard(rowsInline)
                message.replyMarkup = markupInline

                return message
            }

        }

        return null
    }
}