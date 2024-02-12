package ru.mytelegram.financebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import ru.mytelegram.financebot.entity.AccountEntity

interface AccountRepository: JpaRepository<AccountEntity, Long> {

    @Transactional
    fun deleteByNameAndUserId(accountName: String, userId: Long)
    fun findByUserId(userId: Long): List<AccountEntity>
    fun findByNameAndUserId(accountName: String, userId: Long): AccountEntity?
}