package ru.mytelegram.financebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mytelegram.financebot.entity.UserStateEntity

interface UserStateRepository: JpaRepository<UserStateEntity, Long> {
    fun findByUserId(userId: Long): UserStateEntity?
}