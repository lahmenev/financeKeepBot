package ru.mytelegram.financebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mytelegram.financebot.entity.TransactionsEntity
import java.time.OffsetDateTime

interface TransactionsRepository: JpaRepository<TransactionsEntity, Long> {

    @Query("select te from TransactionsEntity te " +
            "join fetch AccountEntity a on te.account.id = a.id " +
            "join fetch CategoryEntity c on te.category.id = c.id " +
            "where a.userId =:userId and te.createdDate >= :fromDate and te.createdDate <= :toDate")
    fun findTransactionsByDate(userId: Long, fromDate: OffsetDateTime, toDate: OffsetDateTime): List<TransactionsEntity>
}