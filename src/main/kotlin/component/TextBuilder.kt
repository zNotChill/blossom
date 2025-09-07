package me.znotchill.blossom.component

import me.znotchill.blossom.extensions.toTextColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.color.Color

class TextBuilder(
    val text: String
) {
    var color: Color = Color(255, 255, 255)
    var bold: Boolean = false
    var italic: Boolean = false
    var underline: Boolean = false
    var strikethrough: Boolean = false
    var obfuscate: Boolean = false

    var clickEvent: ClickEvent? = null

    private var tooltip: Component = Component.empty()

    fun tooltip(text: String, block: TextBuilder.() -> Unit = {}) {
        val builder = TextBuilder(text).apply(block)
        val build = builder.build()

        tooltip = build
    }

    fun onClick(block: OnClickBuilder.() -> Unit) {
        val builder = OnClickBuilder().apply(block)
        clickEvent = builder.event
    }

    fun build(): Component {
        var component = Component.text(text)
            .color(color.toTextColor())
            .decoration(TextDecoration.BOLD, bold)
            .decoration(TextDecoration.ITALIC, italic)
            .decoration(TextDecoration.UNDERLINED, underline)
            .decoration(TextDecoration.STRIKETHROUGH, strikethrough)
            .decoration(TextDecoration.OBFUSCATED, obfuscate)

        clickEvent?.let { component = component.clickEvent(it) }
        tooltip.let { component = component.hoverEvent(HoverEvent.showText(it)) }

        return component
    }
}
