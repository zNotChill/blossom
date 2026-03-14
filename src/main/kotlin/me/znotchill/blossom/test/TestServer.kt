package me.znotchill.blossom.test

import me.znotchill.blossom.server.BlossomServer
import me.znotchill.blossom.server.essentials.BlockPicker
import me.znotchill.blossom.server.essentials.GamemodeSwitcher
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.Instance

internal object TestServer : BlossomServer(
    name = "me.znotchill.blossom.test.TestServer",
) {
    lateinit var instance: Instance
    override fun preLoad() {
        instance = BaseGenerator.new()
        listener<AsyncPlayerConfigurationEvent> { event ->
            event.spawningInstance = instance
            event.player.permissionLevel = 4
        }
        listener<PlayerLoadedEvent> { event ->
            println("hi ${event.player.username}")
            event.player.teleport(Pos(0.0, 50.0, 0.0))
        }

        bareEssential<GamemodeSwitcher> {
            config.permissionLevel = 4
            config.successMessage = { p, g ->
                "Changed ${p.username}'s gamemode to $g"
            }
        }

        bareEssential<BlockPicker> {
            config.gameModes = listOf(GameMode.CREATIVE)
        }
    }
}

internal fun main() {
    TestServer.start()
}