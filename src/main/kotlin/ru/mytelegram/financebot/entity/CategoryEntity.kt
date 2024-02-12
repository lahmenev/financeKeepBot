package ru.mytelegram.financebot.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.OffsetDateTime

@Entity
@Table(schema = "public", name = "category")
@DynamicInsert
@DynamicUpdate
@NamedEntityGraph(
    name = "CategoryEntity.all",
    attributeNodes = [
        NamedAttributeNode("transactions"),
    ]
)
data class CategoryEntity(
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var createdDate: OffsetDateTime? = OffsetDateTime.now(),
    var modifiedDate: OffsetDateTime? = OffsetDateTime.now(),
    val userId: Long,
    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    var transactions: MutableList<TransactionsEntity> = mutableListOf()
)