package me.znotchill.blossom.extensions

import me.znotchill.blossom.bossbar.BossBarManager
import me.znotchill.blossom.bossbar.PlayerBossBar
import me.znotchill.blossom.bossbar.TrackedBossBar
import net.minestom.server.entity.Player

fun Player.bossBar(tracked: TrackedBossBar): PlayerBossBar {
    return PlayerBossBar(this, tracked)
}

fun Player.bossBar(id: String): PlayerBossBar? {
    val tracked = BossBarManager.get(this, id) ?: return null
    return PlayerBossBar(this, tracked)
}

fun Player.hasBossBar(id: String): Boolean {
    return BossBarManager.get(this, id) != null
}

fun Player.hasBossBar(tracked: TrackedBossBar): Boolean {
    return BossBarManager.get(this, tracked.id) != null
}