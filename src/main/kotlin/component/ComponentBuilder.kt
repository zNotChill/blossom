package me.znotchill.blossom.component

import net.kyori.adventure.text.Component

class ComponentBuilder {
    val textBuilders: MutableList<TextBuilder> = mutableListOf()

    fun build(): Component {
        var baseComponent = Component.empty()

        textBuilders.forEach {
            baseComponent = baseComponent.append(it.build())
        }

        return baseComponent
    }

    fun text(text: String, block: TextBuilder.() -> Unit = {}): Component {
        val builder = TextBuilder(text).apply(block)
        val build = builder.build()

        textBuilders.add(builder)
        return build
    }

    fun space() {
        textBuilders.add(TextBuilder(" "))
    }
}