package ru.mytelegram.financebot.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import ru.mytelegram.financebot.entity.CategoryEntity

interface CategoryRepository: JpaRepository<CategoryEntity, Long> {
    fun findByNameAndUserId(categoryName: String, userId: Long): CategoryEntity?
    fun findByUserId(userId: Long): List<CategoryEntity>
    @Transactional
    fun deleteByNameAndUserId(accountName: String, userId: Long)

}