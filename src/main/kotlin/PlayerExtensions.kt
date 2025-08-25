package me.znotchill.blossom

import me.znotchill.blossom.bossbar.BossBarManager
import me.znotchill.blossom.bossbar.PlayerBossBar
import me.znotchill.blossom.bossbar.TrackedBossBar
import net.minestom.server.entity.Player

fun Player.bossBar(tracked: TrackedBossBar): PlayerBossBar {
    return PlayerBossBar(this, tracked)
}

fun Player.bossBar(id: String): PlayerBossBar? {
    val tracked = BossBarManager.get(id) ?: return null
    return PlayerBossBar(this, tracked)
}