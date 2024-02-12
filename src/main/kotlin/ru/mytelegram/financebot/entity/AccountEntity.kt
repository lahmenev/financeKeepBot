package ru.mytelegram.financebot.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(schema = "public", name = "account")
@DynamicInsert
@DynamicUpdate
@NamedEntityGraph(
    name = "AccountEntity.all",
    attributeNodes = [
        NamedAttributeNode("transactions"),
    ]
)
data class AccountEntity(
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val amount: BigDecimal? = null,
    val createdDate: OffsetDateTime? = OffsetDateTime.now(),
    val modifiedDate: OffsetDateTime? = OffsetDateTime.now(),
    val userId: Long,
    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL], orphanRemoval = true)
    val transactions: MutableList<TransactionsEntity> = mutableListOf()
)