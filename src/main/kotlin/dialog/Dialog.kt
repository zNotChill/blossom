package me.znotchill.blossom.dialog

import me.znotchill.blossom.Tags
import net.kyori.adventure.text.Component
import net.minestom.server.dialog.DialogActionButton
import net.minestom.server.dialog.DialogAfterAction
import net.minestom.server.dialog.DialogMetadata
import net.minestom.server.entity.Player
import net.minestom.server.network.packet.server.common.ShowDialogPacket

data class Dialog(
    val type: DialogType,
    val title: Component,
    val canCloseWithEscape: Boolean,
    val afterAction: DialogAfterAction,
    val actions: List<DialogButton>,
    val tabs: List<DialogTab>?,
    val defaultTab: String?,
    var columns: Int
)

enum class DialogType {
    NOTICE,
    TABS
}

data class DialogButton(
    val key: String,
    val button: DialogActionButton,
    val click: (player: Player, tab: DialogButton, dialog: Dialog) -> Unit = { _, _, _ -> },
)

data class DialogTab(
    val key: String,
    var button: DialogActionButton,
    val label: Component = Component.empty(),
    val activeLabel: Component = Component.empty(),
    val tooltip: Component = Component.empty(),
    val activeTooltip: Component = Component.empty(),
    val width: Int = DialogActionButton.DEFAULT_WIDTH,
    val activeWidth: Int = DialogActionButton.DEFAULT_WIDTH,
    var active: Boolean = false,
    val click: (player: Player, tab: DialogTab, dialog: Dialog) -> Unit = { _, _, _ -> },
    val actions: List<DialogButton>
)

class DialogBuilder {
    var type: DialogType = DialogType.NOTICE
    var title: Component = Component.empty()
    var canCloseWithEscape: Boolean = true
    var afterAction: DialogAfterAction = DialogAfterAction.NONE
    var defaultTab: String? = "first"
    var columns: Int = 5

    private val pages = mutableListOf<DialogPage>()

    fun page(index: Int, block: DialogPage.() -> Unit) {
        val page = DialogPage(index).apply(block)
        pages.add(page)
    }

    fun build(): Dialog {
        val actions = pages.flatMap { it.actions }
        val tabs = pages.flatMap { it.tabs }
        return Dialog(
            type,
            title,
            canCloseWithEscape,
            afterAction,
            actions,
            tabs,
            null,
            columns
        )
    }
}

fun Dialog.show(player: Player, firstOpen: Boolean = false) {
    var dialog: net.minestom.server.dialog.Dialog
    when (this.type) {
        DialogType.TABS -> {
            // if we're using tabs, force the column count
            // so we can have one row dedicated for tabs
            columns = tabs?.size ?: 100

            val actionList = mutableListOf<DialogActionButton>()
            val tabQueue = mutableListOf<DialogButton>()

            var activeDefaultTab = ""
            if (firstOpen && (defaultTab == "first" || defaultTab == null)) {
                activeDefaultTab = tabs?.firstOrNull()?.key.toString()
                player.setTag(Tags.CURRENT_TAB, activeDefaultTab)
            }

            tabs?.forEach { tab ->
                var label = tab.label
                var tooltip = tab.tooltip
                var width = tab.width

                if (activeDefaultTab == tab.key) {
                    println(activeDefaultTab)
                    tab.active = true
                }

                if (tab.active) {
                    label = tab.activeLabel
                    tooltip = tab.activeTooltip
                    width = tab.activeWidth

                    tab.actions.forEach {
                        tabQueue.add(it)
                    }
                }

                tab.button = DialogTabsScope.constructActionButton(
                    tab.key,
                    label,
                    tooltip,
                    width
                )

                actionList.add(tab.button)
            }

            tabQueue.forEach {
                actionList.add(it.button)
            }

            dialog = net.minestom.server.dialog.Dialog.MultiAction(
                DialogMetadata(
                    title,
                    null,
                    canCloseWithEscape,
                    false,
                    afterAction,
                    listOf(),
                    listOf()
                ),
                actionList,
                null,
                columns
            )
        }
        else -> {
            dialog = net.minestom.server.dialog.Dialog.MultiAction(
                DialogMetadata(
                    title,
                    null,
                    canCloseWithEscape,
                    false,
                    afterAction,
                    listOf(),
                    listOf()
                ),
                actions.map { it.button },
                null,
                columns
            )
        }
    }

    DialogManager.dialogs[player] = this
    player.sendPacket(
        ShowDialogPacket(dialog)
    )
}

fun Dialog.getTabByKey(key: String): DialogTab? {
    return this.tabs?.firstOrNull { it.key == key }
}

fun DialogTab.getButton(key: String): DialogButton? {
    return this.actions.firstOrNull { it.key == key }
}

fun Dialog.disableTabs() {
    this.tabs?.forEach {
        it.active = false
    }
}