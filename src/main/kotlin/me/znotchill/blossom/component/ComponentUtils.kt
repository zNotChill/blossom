package me.znotchill.blossom.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig

fun Component.replaceVars(vars: Map<String, String>): Component {
    var result = this
    for ((key, value) in vars) {
        result = result.replaceText(
            TextReplacementConfig.builder()
                .matchLiteral("{$key}")
                .replacement(value)
                .build()
        )
    }
    return result
}