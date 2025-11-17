package me.znotchill.blossom.bossbar

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player

class PlayerBossBar(
    private val player: Player,
    private val tracked: TrackedBossBar
) {
    init {
        player.showBossBar(tracked.bar)
    }

    fun update(block: BossBarBuilder.() -> Unit) {
        val updated = BossBarManager.update(player, tracked.id, block) ?: return

        val previousBar = BossBarManager.get(player, tracked.id)
        if (previousBar != null) {
            player.hideBossBar(previousBar.bar)
            MinecraftServer.getBossBarManager().destroyBossBar(previousBar.bar)
        }
        updated.show(player)
    }

    fun hide() {
        val trackedBar = BossBarManager.get(player, tracked.id) ?: return

        player.hideBossBar(trackedBar.bar)
        BossBarManager.hide(player, tracked.id)
    }
}