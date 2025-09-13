package me.znotchill.blossom.key

import net.kyori.adventure.key.Key

class KeyBuilder(
    val name: String,
) {
    fun build(): Key {
        return Key.key(name)
    }
}