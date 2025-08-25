package me.znotchill.blossom.dialog.events

import me.znotchill.blossom.Tags
import me.znotchill.blossom.dialog.DialogManager
import me.znotchill.blossom.dialog.disableTabs
import me.znotchill.blossom.dialog.getButton
import me.znotchill.blossom.dialog.getTabByKey
import me.znotchill.blossom.dialog.show
import net.minestom.server.event.Event
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerCustomClickEvent

class CustomClick {
    fun register(eventNode: EventNode<Event>) {
        eventNode.addListener(
            EventListener.builder(PlayerCustomClickEvent::class.java)
                .handler { event ->
                    val value = event.key.value()
                    val split = value.split(".")
                    val buttonType = split[0]
                    val key = split.drop(1).joinToString(".")

                    val player = event.player
                    val dialog = DialogManager.dialogs[player] ?: return@handler

                    when (buttonType) {
                        "tab_button" -> {
                            val tab = dialog.getTabByKey(key) ?: return@handler

                            tab.click(player, tab, dialog)
                            dialog.disableTabs()
                            tab.active = true

                            player.setTag(Tags.CURRENT_TAB, tab.key)
                            dialog.show(player) // force a re-render
                        }
                        "button" -> {
                            val tabKey = player.getTag(Tags.CURRENT_TAB)
                            val tab = dialog.getTabByKey(tabKey) ?: return@handler

                            val button = tab.getButton(key) ?: return@handler

                            button.click(player, button, dialog)

                        }
                    }
                }.build()
        )
    }
}