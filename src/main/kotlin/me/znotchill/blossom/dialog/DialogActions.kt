package me.znotchill.blossom.dialog

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import net.minestom.server.dialog.DialogAction
import net.minestom.server.dialog.DialogActionButton
import net.minestom.server.entity.Player

class DialogActionsScope() {
    internal val buttons = mutableListOf<DialogButton>()

    fun button(block: DialogButtonScope.() -> Unit) {
        val scope = DialogButtonScope().apply(block)
        buttons += scope.toDialogButton()
    }
}

class DialogButtonScope() {
    var key: String = ""
    var label: Component = Component.empty()
    var tooltip: Component? = null
    var width: Int = DialogActionButton.DEFAULT_WIDTH
    var click: (player: Player, button: DialogButton, dialog: Dialog) -> Unit = { _, _, _ -> }

    internal fun toDialogButton(): DialogButton {
        val fullKey = "button.$key"

        return DialogButton(
            key,
            DialogActionButton(
                label,
                tooltip,
                width,
                DialogAction.DynamicCustom(
                    Key.key(fullKey),
                    CompoundBinaryTag.builder().build()
                )
            ),
            click = click
        )
    }
}
