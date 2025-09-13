package me.znotchill.blossom.command

import net.minestom.server.command.builder.Command

fun command(name: String, block: CommandBuilder.() -> Unit): Command {
    val cmd = Command(name)
    CommandBuilder(cmd).apply(block)
    return cmd
}