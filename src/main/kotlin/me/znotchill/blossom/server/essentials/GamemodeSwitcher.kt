package me.znotchill.blossom.server.essentials

import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.extensions.sendMessage
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.blossom.server.essentials.classes.Essential
import me.znotchill.blossom.server.essentials.classes.EssentialConfig
import net.kyori.adventure.text.Component
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerGameModeRequestEvent

data class GamemodeSwitcherConfig(
    var permissionLevel: Int = 4,
    var successMessage: (Player, GameMode) -> Any = { _, _ -> Component.empty() },
    var permissionMessage: (Player, GameMode) -> Any = { _, _ -> Component.empty() },
) : EssentialConfig

class GamemodeSwitcher(
    override val config: GamemodeSwitcherConfig = GamemodeSwitcherConfig()
) : Essential<GamemodeSwitcherConfig>, EssentialConfig by config
{
    override fun load(server: BlossomServer) {
        server.eventHandler.addListener<PlayerGameModeRequestEvent> { event ->
            if (event.player.permissionLevel >= config.permissionLevel) {
                event.player.setGameMode(event.requestedGameMode)

                event.player.sendMessage(
                    config.successMessage(event.player, event.requestedGameMode)
                )
                return@addListener
            }

            event.player.sendMessage(
                config.permissionMessage(event.player, event.requestedGameMode)
            )
        }
    }
}