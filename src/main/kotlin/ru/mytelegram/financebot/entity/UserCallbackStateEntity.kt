package ru.mytelegram.financebot.entity

import jakarta.persistence.*
import ru.mytelegram.financebot.handler.CallbackStateEnum
import java.time.OffsetDateTime

@Entity
@Table(schema = "public", name = "user_callback_state")
data class UserCallbackStateEntity(
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val callbackState: CallbackStateEnum,
    val value: String? = null,
    val userStateId: Long,
    val createdDate: OffsetDateTime? = OffsetDateTime.now(),
    val modifiedDate: OffsetDateTime? = OffsetDateTime.now(),
)