package ru.mytelegram.financebot.handler

enum class CallbackStateEnum(
    val description: String
) {
    SELECT_CATEGORY_CALLBACK("Выберите категорию"),
    SELECT_ACCOUNT_CALLBACK("Выберите счет"),
    PUT_AMOUNT("Введите сумму");
}