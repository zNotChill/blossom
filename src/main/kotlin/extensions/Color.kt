package me.znotchill.blossom.extensions

import net.kyori.adventure.text.format.TextColor
import net.minestom.server.color.Color

fun Color.toTextColor(): TextColor {
    return TextColor.color(
        this
    )
}