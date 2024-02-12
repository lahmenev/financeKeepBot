package ru.mytelegram.financebot.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

interface CommandHandler {
    fun getCommandType(): CommandEnum
    fun handle(update: Update, userId: Long): SendMessage?
}