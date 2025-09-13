package me.znotchill.blossom.bossbar

fun bossBar(id: String, block: BossBarBuilder.() -> Unit): TrackedBossBar {
    val builder = BossBarBuilder().apply(block)
    val bar = builder.build()
    return TrackedBossBar(id, bar)
}
