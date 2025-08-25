package me.znotchill.blossom.bossbar

import net.kyori.adventure.bossbar.BossBar

object BossBarManager {
    private val bars = mutableMapOf<String, BossBar>()

    fun get(id: String): TrackedBossBar? =
        bars[id]?.let { TrackedBossBar(id, it) }

    fun set(bar: TrackedBossBar) {
        bars[bar.id] = bar.bar
    }

    fun update(id: String, block: BossBarBuilder.() -> Unit): TrackedBossBar? {
        val existing = bars[id] ?: return null
        val builder = BossBarBuilder.from(existing).apply(block)
        val updated = builder.build()
        val newBar = TrackedBossBar(id, updated)
        return newBar
    }
}
