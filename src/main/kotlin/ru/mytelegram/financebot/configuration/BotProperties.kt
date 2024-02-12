package ru.mytelegram.financebot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot")
data class BotProperties(
   val name: String,
   val token: String
)