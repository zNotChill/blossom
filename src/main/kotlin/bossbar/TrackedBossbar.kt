package me.znotchill.blossom.bossbar

import net.kyori.adventure.bossbar.BossBar
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player

data class TrackedBossBar(
    val id: String,
    val bar: BossBar
)

fun TrackedBossBar.show(player: Player) {
    val previousBar = BossBarManager.get(this.id)
    if (previousBar != null) {
        player.hideBossBar(previousBar.bar)
        MinecraftServer.getBossBarManager().destroyBossBar(previousBar.bar)
    }

    player.showBossBar(this.bar)
    BossBarManager.set(this)
}