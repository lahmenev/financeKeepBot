package ru.mytelegram.financebot.handler.category

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.entity.CategoryEntity
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.CategoryRepository

@Service
class AddCategoryHandler(
    private val categoryRepository: CategoryRepository
): CommandHandler {

    override fun getCommandType(): CommandEnum {
        return CommandEnum.ADD_CATEGORY
    }

    override fun handle(update: Update, userId: Long): SendMessage? {
        if (update.hasCallbackQuery()) {
            val callbackData = update.callbackQuery.data
            if (callbackData.equals(CommandEnum.ADD_CATEGORY.name)) {
                val message = SendMessage()
                message.chatId = userId.toString()
                message.text = "Введите название категории"
                return message
            }

        } else {
            val categoryName = update.getMessage().text

            val categoryEntity = CategoryEntity(
                name = categoryName,
                userId = userId
            )

            categoryRepository.save(categoryEntity)

            val markupInline = InlineKeyboardMarkup()
            val rowsInline = mutableListOf<List<InlineKeyboardButton>>()
            val rowInline = mutableListOf<InlineKeyboardButton>()

            val keyboardBtn = InlineKeyboardButton()
            keyboardBtn.text = "Назад в меню"
            keyboardBtn.callbackData = "${CommandEnum.START_COMMAND}"
            rowInline.add(keyboardBtn)

            rowsInline.add(rowInline)
            markupInline.setKeyboard(rowsInline)

            val message = SendMessage()
            message.chatId = userId.toString()
            message.replyMarkup = markupInline
            message.text = "Успешно добавлена категория: $categoryName"

            return message
        }

        return null
    }
}