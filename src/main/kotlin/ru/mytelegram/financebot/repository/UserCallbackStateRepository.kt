package ru.mytelegram.financebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.mytelegram.financebot.entity.UserCallbackStateEntity
import ru.mytelegram.financebot.handler.CallbackStateEnum

interface UserCallbackStateRepository: JpaRepository<UserCallbackStateEntity, Long> {

    @Query("select ce from UserCallbackStateEntity ce " +
            "join fetch UserStateEntity se on ce.userStateId = se.id " +
            "where se.userId = :userId")
    fun findByUserId(userId: Long): List<UserCallbackStateEntity>
    fun findByCallbackState(callbackState: CallbackStateEnum): List<UserCallbackStateEntity>
    @Transactional
    fun deleteByUserStateId(userTateId: Long)
}