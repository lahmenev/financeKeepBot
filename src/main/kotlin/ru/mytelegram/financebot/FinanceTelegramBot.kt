package ru.mytelegram.financebot

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import ru.mytelegram.financebot.handler.FinanceTelegramHandler

@Service
@Slf4j
class FinanceTelegramBot(
    private val financeTelegramHandler: FinanceTelegramHandler
): TelegramLongPollingBot() {

    @Value("\${bot.name}")
    private lateinit var username: String
    @Value("\${bot.token}")
    private lateinit var token: String


    override fun getBotUsername(): String {
        return username
    }

    override fun onUpdateReceived(update: Update) {
        val message = financeTelegramHandler.handle(update)
        execute(message)
    }

    override fun getBotToken(): String {
        return token
    }
}