package me.znotchill.blossom.server.essentials

import me.znotchill.blossom.component.replaceVars
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.blossom.server.essentials.classes.Essential
import me.znotchill.blossom.server.essentials.classes.EssentialConfig
import net.kyori.adventure.text.Component
import net.minestom.server.event.player.PlayerGameModeRequestEvent

data class GamemodeSwitcherConfig(
    val permissionLevel: Int = 4,
    val successMessage: Component? = null,
    val permissionMessage: Component? = null,
) : EssentialConfig

class GamemodeSwitcher(
    override val config: GamemodeSwitcherConfig
) : Essential<GamemodeSwitcherConfig> {
    override fun load(server: BlossomServer) {
        server.eventHandler.addListener<PlayerGameModeRequestEvent> { event ->
            if (event.player.permissionLevel >= config.permissionLevel) {
                event.player.setGameMode(event.requestedGameMode)

                config.successMessage?.let { msg ->
                    val replaced = msg.replaceVars(
                        mapOf(
                            "GAMEMODE" to event.requestedGameMode.name,
                            "PLAYER" to event.player.username
                        )
                    )

                    event.player.sendMessage(replaced)
                }
                return@addListener
            }

            config.permissionMessage?.let { msg ->
                val replaced = msg.replaceVars(
                    mapOf(
                        "PLAYER" to event.player.username
                    )
                )

                event.player.sendMessage(replaced)
            }
        }
    }
}