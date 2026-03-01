package me.znotchill.blossom.extensions

import net.kyori.adventure.text.Component
import net.minestom.server.command.CommandSender

fun CommandSender.sendMessage(message: Any) {
    when (message) {
        is Component -> sendMessage(message)
        else -> sendMessage(message.toString())
    }
}