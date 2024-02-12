package ru.mytelegram.financebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mytelegram.financebot.entity.UsersEntity

interface UserRepository: JpaRepository<UsersEntity, Long> {
    fun findByChatId(chatId: Long): UsersEntity?
}