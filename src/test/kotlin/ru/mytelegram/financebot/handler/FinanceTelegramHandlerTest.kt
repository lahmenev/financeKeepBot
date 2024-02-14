package ru.mytelegram.financebot.handler

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.telegram.telegrambots.meta.api.objects.Update
import ru.mytelegram.financebot.repository.UserRepository
import ru.mytelegram.financebot.repository.UserStateRepository

@ExtendWith(MockKExtension::class)
class FinanceTelegramHandlerTest {
    @InjectMockKs
    private lateinit var financeTelegramHandler: FinanceTelegramHandler
    @MockK
    private lateinit var commandHandlerFactory: CommandHandlerFactory
    @MockK
    private lateinit var userRepository: UserRepository
    @MockK
    private lateinit var userStateRepository: UserStateRepository

    @Test
    fun `handle test`() {
        val easyRandom = EasyRandom()
        val message = Update()

        val handle = financeTelegramHandler.handle(message)
        Assertions.assertTrue(false)
    }
}