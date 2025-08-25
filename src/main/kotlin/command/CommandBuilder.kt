package me.znotchill.blossom.command

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player

class CommandBuilder(val command: Command) {
    inline fun <reified T : CommandSender> default(
        noinline handler: T.(CommandContext) -> Unit
    ) {
        command.setDefaultExecutor { sender, context ->
            if (sender is T) handler(sender, context)
        }
    }

    inline fun <reified T> argument(name: String): Argument<T> {
        return when (T::class) {
            String::class -> ArgumentType.String(name) as Argument<T>
            Int::class -> ArgumentType.Integer(name) as Argument<T>
            Boolean::class -> ArgumentType.Boolean(name) as Argument<T>
            Double::class -> ArgumentType.Double(name) as Argument<T>
            else -> error("Unsupported argument type: ${T::class}")
        }
    }

    inline fun <reified A> syntax(
        name: String,
        crossinline handler: Player.(A) -> Unit
    ) {
        val argA = argument<A>(name)
        command.addSyntax({ sender, ctx ->
            if (sender is Player) {
                handler(sender, ctx.get(argA))
            }
        }, argA)
    }

    inline fun <reified A, reified B> syntax(
        nameA: String,
        nameB: String,
        crossinline handler: Player.(A, B) -> Unit
    ) {
        val argA = argument<A>(nameA)
        val argB = argument<B>(nameB)
        command.addSyntax({ sender, ctx ->
            if (sender is Player) {
                handler(sender, ctx.get(argA), ctx.get(argB))
            }
        }, argA, argB)
    }

    inline fun <reified A, reified B, reified C> syntax(
        nameA: String,
        nameB: String,
        nameC: String,
        crossinline handler: Player.(A, B, C) -> Unit
    ) {
        val argA = argument<A>(nameA)
        val argB = argument<B>(nameB)
        val argC = argument<C>(nameC)
        command.addSyntax({ sender, ctx ->
            if (sender is Player) {
                handler(sender, ctx.get(argA), ctx.get(argB), ctx.get(argC))
            }
        }, argA, argB, argC)
    }

    fun build(): Command = command
}