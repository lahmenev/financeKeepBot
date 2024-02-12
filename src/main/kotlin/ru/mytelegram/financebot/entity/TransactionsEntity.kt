package ru.mytelegram.financebot.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "transactions")
data class TransactionsEntity(
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    val category: CategoryEntity? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    val account: AccountEntity,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val type: Type,
    val createdDate: OffsetDateTime? = OffsetDateTime.now(),
    val modifiedDate: OffsetDateTime? = OffsetDateTime.now(),
) {
    enum class Type {
        ADD,
        SUBTRACT
    }
}