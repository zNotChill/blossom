package me.znotchill.blossom.dialog

import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import net.minestom.server.dialog.DialogAction
import net.minestom.server.dialog.DialogActionButton
import net.minestom.server.entity.Player

class DialogTabsScope {
    internal val tabs = mutableListOf<DialogTab>()

    fun tab(block: DialogTabScope.() -> Unit) {
        val scope = DialogTabScope().apply(block)
        tabs += scope.toDialogTab()
    }

    companion object {
        fun constructActionButton(
            key: String,
            label: Component = Component.empty(),
            tooltip: Component = Component.empty(),
            width: Int = DialogActionButton.DEFAULT_WIDTH,
        ): DialogActionButton {
            return DialogActionButton(
                label,
                tooltip,
                width,
                DialogAction.DynamicCustom(
                    Key.key("tab_button.$key"),
                    CompoundBinaryTag.builder().build()
                )
            )
        }
    }
}

class DialogTabScope() {
    var key: String = ""
    var label: Component = Component.empty()
    var activeLabel: Component = Component.empty()
    var tooltip: Component = Component.empty()
    var activeTooltip: Component = Component.empty()
    var width: Int = DialogActionButton.DEFAULT_WIDTH
    var activeWidth: Int = DialogActionButton.DEFAULT_WIDTH
    var click: (player: Player, tab: DialogTab, dialog: Dialog) -> Unit = { _, _, _ -> }
    internal val actions = mutableListOf<DialogButton>()

    fun actions(block: DialogActionsScope.() -> Unit) {
        val scope = DialogActionsScope().apply(block)
        actions.addAll(scope.buttons)
    }

    internal fun toDialogTab(): DialogTab {
        return DialogTab(
            key,
            DialogTabsScope.constructActionButton(key, label, tooltip, width),
            label = label,
            activeLabel = activeLabel,
            tooltip = tooltip,
            activeTooltip = activeTooltip,
            width = width,
            activeWidth = activeWidth,
            active = false,
            actions = actions.toList(),
            click = click
        )
    }
}
