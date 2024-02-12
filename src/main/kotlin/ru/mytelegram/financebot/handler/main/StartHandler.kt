package ru.mytelegram.financebot.handler.main

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler

@Service
class StartHandler: CommandHandler {

    override fun getCommandType(): CommandEnum {
        return CommandEnum.START_COMMAND
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        val message = SendMessage()
        message.chatId = userId.toString()
        message.text = "Добро пожаловать. Выберите одну из команд ниже:"

        val markupInline = InlineKeyboardMarkup()
        val rowsInline = mutableListOf<List<InlineKeyboardButton>>()

        CommandEnum.values().filter { it.isMain }.forEach {
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