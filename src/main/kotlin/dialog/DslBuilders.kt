package me.znotchill.blossom.dialog

fun dialog(block: DialogBuilder.() -> Unit): Dialog {
    return DialogBuilder().apply(block).build()
}