package me.znotchill.blossom

import me.znotchill.blossom.bossbar.BossBarBuilder
import me.znotchill.blossom.bossbar.TrackedBossBar
import me.znotchill.blossom.command.CommandBuilder
import me.znotchill.blossom.component.ComponentBuilder
import net.kyori.adventure.text.Component
import net.minestom.server.command.builder.Command

fun command(name: String, block: CommandBuilder.() -> Unit): Command {
    val cmd = Command(name)
    CommandBuilder(cmd).apply(block)
    return cmd
}

fun bossBar(id: String, block: BossBarBuilder.() -> Unit): TrackedBossBar {
    val builder = BossBarBuilder().apply(block)
    val bar = builder.build()
    return TrackedBossBar(id, bar)
}

fun component(block: ComponentBuilder.() -> Unit): Component {
    val builder = ComponentBuilder().apply(block)
    return builder.build()
}