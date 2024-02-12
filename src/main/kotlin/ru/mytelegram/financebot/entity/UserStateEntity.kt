package ru.mytelegram.financebot.entity

import jakarta.persistence.*
import ru.mytelegram.financebot.handler.CommandEnum
import java.time.OffsetDateTime

@Entity
@Table(name = "user_state")
data class UserStateEntity(
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val userId: Long,
    @Enumerated(EnumType.STRING)
    val lastState: CommandEnum? = null,
    val createdDate: OffsetDateTime? = OffsetDateTime.now(),
    val modifiedDate: OffsetDateTime? = OffsetDateTime.now(),
)