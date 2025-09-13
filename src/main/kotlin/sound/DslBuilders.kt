package me.znotchill.blossom.sound

import me.znotchill.blossom.key.key
import net.kyori.adventure.sound.Sound

fun sound(name: String, block: SoundBuilder.() -> Unit): Sound {
    val builder = SoundBuilder(key(name)).apply(block)
    return builder.build()
}

fun sound(name: String): Sound {
    val builder = SoundBuilder(key(name))
    return builder.build()
}