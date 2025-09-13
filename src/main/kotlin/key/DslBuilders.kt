package me.znotchill.blossom.key

import net.kyori.adventure.key.Key

fun key(name: String): Key {
    val builder = KeyBuilder(name)
    return builder.build()
}
