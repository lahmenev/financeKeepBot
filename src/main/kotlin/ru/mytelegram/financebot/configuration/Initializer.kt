package ru.mytelegram.financebot.configuration

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.mytelegram.financebot.FinanceTelegramBot

@Component
//@Slf4j
class Initializer(
    private val financeTelegramBot: FinanceTelegramBot
) {

    @EventListener(*[ContextRefreshedEvent::class])
    fun init() {
        try {
            val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
            telegramBotsApi.registerBot(financeTelegramBot as LongPollingBot?)
        } catch (e: TelegramApiException) {
//            log().error(e.getMessage())
        }
    }
}