package ru.mytelegram.financebot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.mytelegram.financebot.configuration.BotProperties


@SpringBootApplication(scanBasePackages = arrayOf("ru.mytelegram.financebot"))
@EnableConfigurationProperties(BotProperties::class)
class FinanceBotApplication

fun main(args: Array<String>) {
    runApplication<FinanceBotApplication>(*args)
}

