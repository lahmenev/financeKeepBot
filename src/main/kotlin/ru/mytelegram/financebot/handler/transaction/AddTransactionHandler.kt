package ru.mytelegram.financebot.handler.transaction

import org.springframework.stereotype.Service
import ru.mytelegram.financebot.handler.CommandEnum
import ru.mytelegram.financebot.repository.*

@Service
class AddTransactionHandler(
    accountRepository: AccountRepository,
    categoryRepository: CategoryRepository,
    transactionsRepository: TransactionsRepository,
    userStateRepository: UserStateRepository,
    userCallbackStateRepository: UserCallbackStateRepository
) : AbstractTransactionHandler(
    accountRepository,
    categoryRepository,
    transactionsRepository,
    userStateRepository,
    userCallbackStateRepository
) {

    override fun getCommandType(): CommandEnum {
        return CommandEnum.ADD_TRANSACTION
    }

}