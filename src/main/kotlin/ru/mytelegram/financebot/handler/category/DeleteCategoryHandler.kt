package ru.mytelegram.financebot.handler.category

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.handler.CallbackStateEnum
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.CategoryRepository

@Service
class DeleteCategoryHandler(
    private val categoryRepository: CategoryRepository
): CommandHandler {

    override fun getCommandType(): CommandEnum {
        return CommandEnum.DELETE_CATEGORY
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.DELETE_CATEGORY.name)) {
            val message = SendMessage()
            message.chatId = userId.toString()
            message.text = CallbackStateEnum.SELECT_CATEGORY_CALLBACK.description

            val markupInline = InlineKeyboardMarkup()
            val rowsInline = mutableListOf<List<InlineKeyboardButton>>()
            val rowInline = mutableListOf<InlineKeyboardButton>()

            val categories = categoryRepository.findByUserId(userId)

            if (!categories.isEmpty()) {
                categories.map { it.name }.forEach {
                    val keyboardBtn = InlineKeyboardButton()
                    keyboardBtn.text = it
                    keyboardBtn.callbackData = "${CallbackStateEnum.SELECT_CATEGORY_CALLBACK.name}:$it"
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
                message.text = "Список категорий пуст"
            }

            return message

        } else if (update.hasCallbackQuery() && update.callbackQuery.data.contains(CallbackStateEnum.SELECT_CATEGORY_CALLBACK.name)) {
            val categoryName = update.callbackQuery.data.substring(update.callbackQuery.data.indexOf(":") + 1)
            val chatId: Long = update.callbackQuery.message.chatId

            categoryRepository.deleteByNameAndUserId(categoryName, userId)

            val message = SendMessage()
            message.chatId = chatId.toString()
            message.text = "Категория $categoryName успешно удалена"

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