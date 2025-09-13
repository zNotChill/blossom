package me.znotchill.blossom.component

import net.kyori.adventure.text.Component

fun component(block: ComponentBuilder.() -> Unit): Component {
    val builder = ComponentBuilder().apply(block)
    return builder.build()
}