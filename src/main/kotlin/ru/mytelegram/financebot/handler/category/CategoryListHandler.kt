package ru.mytelegram.financebot.handler.category

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.CategoryRepository

@Service
class CategoryListHandler(
    private val categoryRepository: CategoryRepository
): CommandHandler {
    override fun getCommandType(): CommandEnum {
        return CommandEnum.GET_CATEGORIES
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        val categories = categoryRepository.findByUserId(userId)

        val message = SendMessage()
        message.chatId = userId.toString()
        message.text = if (!categories.isNullOrEmpty())
            categories.map { it.name }.toMutableList().joinToString(separator = "\n")
        else
            "список категорий пуст"

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