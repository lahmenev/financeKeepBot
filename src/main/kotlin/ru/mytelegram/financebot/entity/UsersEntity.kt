package ru.mytelegram.financebot.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "users")
data class UsersEntity(
    @Id
    @Column(unique = true)
    val chatId: Long,
    val name: String? = null,
    val surname: String? = null,
    val login: String? = null,
    val createdDate: OffsetDateTime? = OffsetDateTime.now(),
    val modifiedDate: OffsetDateTime? = OffsetDateTime.now()
)