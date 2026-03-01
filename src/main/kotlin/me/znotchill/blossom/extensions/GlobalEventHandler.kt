package me.znotchill.blossom.extensions

import me.znotchill.blossom.server.BlossomServer
import net.minestom.server.event.Event
import net.minestom.server.event.GlobalEventHandler

inline fun <reified T : Event> GlobalEventHandler.addListener(
    noinline callback: (T) -> Unit
) {
    this.addListener(T::class.java) { event ->
        callback(event)
    }
}