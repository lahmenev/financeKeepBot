package ru.mytelegram.financebot.handler.transaction

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.mytelegram.financebot.entity.TransactionsEntity
import ru.mytelegram.financebot.entity.UserCallbackStateEntity
import ru.mytelegram.financebot.handler.CallbackStateEnum
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.handler.CommandHandler
import ru.mytelegram.financebot.repository.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
abstract class AbstractTransactionHandler(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionsRepository: TransactionsRepository,
    private val userStateRepository: UserStateRepository,
    private val userCallbackStateRepository: UserCallbackStateRepository
): CommandHandler {

    @Transactional
    override fun handle(update: Update, userId: Long): SendMessage? {
        val commandType = getCommandType()

        if (update.hasCallbackQuery()) {
            val callbackData = update.callbackQuery.data

            if (callbackData.equals(CommandEnum.SUBTRACT_TRANSACTION.name) ||
                callbackData.equals(CommandEnum.ADD_TRANSACTION.name)) {
                val message = SendMessage()
                message.text = CallbackStateEnum.SELECT_ACCOUNT_CALLBACK.description
                message.chatId = userId.toString()

                val markupInline = InlineKeyboardMarkup()
                val rowsInline = mutableListOf<List<InlineKeyboardButton>>()
                val rowInline = mutableListOf<InlineKeyboardButton>()

                val accounts = accountRepository.findByUserId(userId)

                accounts.map { it.name }.forEach {
                    val keyboardBtn = InlineKeyboardButton()
                    keyboardBtn.text = it
                    keyboardBtn.callbackData = "${CallbackStateEnum.SELECT_ACCOUNT_CALLBACK.name}:$it"
                    rowInline.add(keyboardBtn)
                }
                rowsInline.add(rowInline)
                markupInline.setKeyboard(rowsInline)
                message.replyMarkup = markupInline

                return message
            }
            if (callbackData.contains(CallbackStateEnum.SELECT_ACCOUNT_CALLBACK.name)) {
                val account = callbackData.substring(callbackData.indexOf(":") + 1)
                val userState = userStateRepository.findByUserId(userId)

                userCallbackStateRepository.save(
                    UserCallbackStateEntity(
                        callbackState = CallbackStateEnum.SELECT_ACCOUNT_CALLBACK,
                        value = account,
                        userStateId = userState?.id!!
                    )
                )

                val message = SendMessage()

                if (commandType.equals(CommandEnum.SUBTRACT_TRANSACTION)) {
                    message.text = CallbackStateEnum.SELECT_CATEGORY_CALLBACK.description
                    message.chatId = userId.toString()

                    val markupInline = InlineKeyboardMarkup()
                    val rowsInline = mutableListOf<List<InlineKeyboardButton>>()
                    val rowInline = mutableListOf<InlineKeyboardButton>()

                    val categories = categoryRepository.findAll()

                    categories.map {it.name}.forEach {
                        val keyboardBtn = InlineKeyboardButton()
                        keyboardBtn.text = it
                        keyboardBtn.callbackData = "${CallbackStateEnum.SELECT_CATEGORY_CALLBACK.name}:$it"
                        rowInline.add(keyboardBtn)
                        rowsInline.add(rowInline)
                    }

                    markupInline.setKeyboard(rowsInline)
                    message.replyMarkup = markupInline
                } else {
                    message.text = CallbackStateEnum.PUT_AMOUNT.description
                    message.chatId = userId.toString()
                }

                return message
            }

            if (callbackData.contains(CallbackStateEnum.SELECT_CATEGORY_CALLBACK.name)) {
                val category = callbackData.substring(callbackData.indexOf(":") + 1)
                val userState = userStateRepository.findByUserId(userId)

                userCallbackStateRepository.save(
                    UserCallbackStateEntity(
                        callbackState = CallbackStateEnum.SELECT_CATEGORY_CALLBACK,
                        value = category,
                        userStateId = userState?.id!!
                    )
                )

                val message = SendMessage()
                message.text = CallbackStateEnum.PUT_AMOUNT.description
                message.chatId = userId.toString()

                return message
            }
        } else {
            val lastCallbackState = userCallbackStateRepository.findByUserId(userId)
                .sortedByDescending { it.modifiedDate }.first()
            val subtractStatement = lastCallbackState.callbackState.equals(CallbackStateEnum.SELECT_CATEGORY_CALLBACK) &&
                    getCommandType().equals(CommandEnum.SUBTRACT_TRANSACTION)
            val addStatement = lastCallbackState.callbackState.equals(CallbackStateEnum.SELECT_ACCOUNT_CALLBACK) &&
                    getCommandType().equals(CommandEnum.ADD_TRANSACTION)

            if (subtractStatement || addStatement) {
                val value = update.getMessage().getText().toLong()
                val account = userCallbackStateRepository.findByCallbackState(CallbackStateEnum.SELECT_ACCOUNT_CALLBACK).first()

                val category = when(commandType) {
                    CommandEnum.SUBTRACT_TRANSACTION -> userCallbackStateRepository.findByCallbackState(CallbackStateEnum.SELECT_CATEGORY_CALLBACK).first()
                    else -> null
                }

                val type = when(commandType) {
                    CommandEnum.SUBTRACT_TRANSACTION -> TransactionsEntity.Type.SUBTRACT
                    else -> TransactionsEntity.Type.ADD
                }

                val accountEntity = accountRepository.findByNameAndUserId(account.value!!, userId)!!
                val categoryEntity = category?.let { categoryRepository.findByNameAndUserId(it.value!!, userId) }

                val updatedAccountAmount = when(commandType) {
                    CommandEnum.SUBTRACT_TRANSACTION -> accountEntity.amount?.minus(BigDecimal(value))
                    else -> accountEntity.amount?.plus(BigDecimal(value))
                }

                val transactionEntity = TransactionsEntity(
                    category = categoryEntity,
                    account = accountEntity,
                    amount = BigDecimal(value),
                    type = type
                )

                val updatedAccount = accountEntity.copy(
                    amount = updatedAccountAmount,
                    modifiedDate = OffsetDateTime.now()
                )
                transactionsRepository.save(transactionEntity)
                accountRepository.save(updatedAccount)

                val userStateId = userStateRepository.findByUserId(userId)?.id!!
                userCallbackStateRepository.deleteByUserStateId(userStateId)

                val message = SendMessage()
                message.chatId = update.message.chatId.toString()
                message.text = "Операция выполнена успешно"

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

        return null
    }
}