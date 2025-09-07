package me.znotchill.blossom.component

import net.kyori.adventure.dialog.DialogLike
import net.kyori.adventure.text.event.ClickEvent

class OnClickBuilder {
    var event: ClickEvent? = null

    fun runCommand(command: String) {
        event = ClickEvent.runCommand(command)
    }

    fun suggestCommand(command: String) {
        event = ClickEvent.suggestCommand(command)
    }

    fun openUrl(url: String) {
        event = ClickEvent.openUrl(url)
    }

    fun changePage(page: Int) {
        event = ClickEvent.changePage(page)
    }

    fun copyToClipboard(text: String) {
        event = ClickEvent.copyToClipboard(text)
    }

    fun showDialog(dialog: DialogLike) {
        event = ClickEvent.showDialog(dialog)
    }
}
