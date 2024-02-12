package ru.mytelegram.financebot.handler

enum class CommandEnum(
    val description: String,
    val isMain: Boolean = false
) {
    START_COMMAND("Главная страница", true),
    ADD_TRANSACTION("Пополнение", true),
    SUBTRACT_TRANSACTION("Списание", true),
    ACCOUNT_COMMAND("Операции со счетами", true),
    CATEGORY_COMMAND("Операции с категориями операций", true),

    GET_ACCOUNTS("Получить список счетов"),
    ADD_ACCOUNT("Добавить счет"),
    DELETE_ACCOUNT("Удалить счет"),
    UPDATE_ACCOUNT("Обновить счет"),

    GET_CATEGORIES("Получить список категорий"),
    ADD_CATEGORY("Добавить категорию"),
    DELETE_CATEGORY("Удалить категорию"),

    GET_ACCOUNTS_AMOUNT_SUM("Получить общую сумму", true),
    GET_EXPENSES("Получить расходы с начала месяца", true)

}