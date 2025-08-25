package me.znotchill.blossom.bossbar

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component

class BossBarBuilder {
    var text: Component = Component.text("")
    var progress: Float = 0f
    var color: BossBar.Color = BossBar.Color.PURPLE
    var overlay: BossBar.Overlay = BossBar.Overlay.PROGRESS

    fun build(): BossBar {
        return BossBar.bossBar(text, progress, color, overlay)
    }

    companion object {
        fun from(bar: BossBar): BossBarBuilder = BossBarBuilder().apply {
            text = bar.name()
            progress = bar.progress()
            color = bar.color()
            overlay = bar.overlay()
        }
    }
}