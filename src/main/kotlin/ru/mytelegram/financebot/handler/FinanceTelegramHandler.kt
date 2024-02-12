package ru.mytelegram.financebot.handler

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.mytelegram.financebot.entity.UserStateEntity
import ru.mytelegram.financebot.entity.UsersEntity
import ru.mytelegram.financebot.repository.UserRepository
import ru.mytelegram.financebot.repository.UserStateRepository

@Service
class FinanceTelegramHandler(
    private val commandHandlerFactory: CommandHandlerFactory,
    private val userRepository: UserRepository,
    private val userStateRepository: UserStateRepository
) {

    fun handle(update: Update): SendMessage? {
        if ((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {
            val chatId = update.getMessage()?.chatId ?: update.callbackQuery.from.id

            if (update.hasMessage() && update.getMessage().getText().equals("/mainmenu")) {

                userRepository.findByChatId(chatId) ?:
                userRepository.save(UsersEntity(
                    chatId = chatId,
                    name = update.getMessage().from.firstName,
                    surname = update.getMessage().from.lastName,
                    login = update.getMessage().from.userName
                ))
            }

            val currentState = getCurrentState(update, chatId)
            val lastStateEntity = userStateRepository.findByUserId(chatId)

            if (!currentState.equals(lastStateEntity?.lastState)) {
                val updatedState = lastStateEntity ?: UserStateEntity(
                    userId = chatId,
                    lastState = currentState
                )

                userStateRepository.save(updatedState.copy(lastState = currentState))
            }

            val commandHandler = commandHandlerFactory.findHandler(currentState)
            return commandHandler.handle(update, chatId)
        }

        return null
    }

    private fun getCurrentState(update: Update, userId: Long): CommandEnum {


        return if (update.hasMessage() && update.getMessage().getText().equals("/mainmenu") ||
            update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.START_COMMAND.name)) {
            CommandEnum.START_COMMAND
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.SUBTRACT_TRANSACTION.name)) {
            CommandEnum.SUBTRACT_TRANSACTION
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.ADD_TRANSACTION.name)) {
            CommandEnum.ADD_TRANSACTION
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.ACCOUNT_COMMAND.name)) {
            CommandEnum.ACCOUNT_COMMAND
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.CATEGORY_COMMAND.name)) {
            CommandEnum.CATEGORY_COMMAND
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.ADD_ACCOUNT.name)) {
            CommandEnum.ADD_ACCOUNT
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.DELETE_ACCOUNT.name)) {
            CommandEnum.DELETE_ACCOUNT
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.GET_ACCOUNTS.name)) {
            CommandEnum.GET_ACCOUNTS
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.GET_CATEGORIES.name)) {
            CommandEnum.GET_CATEGORIES
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.ADD_CATEGORY.name)) {
            CommandEnum.ADD_CATEGORY
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.DELETE_CATEGORY.name)) {
            CommandEnum.DELETE_CATEGORY
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.GET_ACCOUNTS_AMOUNT_SUM.name)) {
            CommandEnum.GET_ACCOUNTS_AMOUNT_SUM
        } else if (update.hasCallbackQuery() && update.callbackQuery.data.equals(CommandEnum.GET_EXPENSES.name)) {
            CommandEnum.GET_EXPENSES
        }

        else {
            userStateRepository.findByUserId(userId)?.lastState!!
        }
    }
}
