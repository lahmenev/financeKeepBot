package ru.mytelegram.financebot.handler

import org.springframework.stereotype.Service

@Service
class CommandHandlerFactory(
    private val handlers: List<CommandHandler>
) {

    var map = mutableMapOf<CommandEnum, CommandHandler>()

    init {
        handlers.forEach{ map[it.getCommandType()] = it }
    }

    fun findHandler(command: CommandEnum): CommandHandler =
        map[command]!!
}