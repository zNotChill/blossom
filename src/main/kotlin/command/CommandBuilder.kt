package me.znotchill.blossom.command

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import net.minestom.server.entity.Player
import java.util.UUID

class CommandBuilder(val command: Command) {
    inline fun <reified T : CommandSender> default(
        noinline handler: T.(CommandContext) -> Unit
    ) {
        command.setDefaultExecutor { sender, context ->
            if (sender is T) handler(sender, context)
        }
    }

    class ArgumentBuilder<T>(val argument: Argument<T>) {
        var defaultValue: T? = null

        fun onSuggestion(block: (sender: CommandSender, ctx: CommandContext) -> List<Any>) {
            argument.setSuggestionCallback { sender, ctx, suggestion ->
                block(sender, ctx).forEach {
                    suggestion.addEntry(SuggestionEntry(it.toString()))
                }
            }
        }

        fun onSuggestion(block: () -> List<Any>) {
            argument.setSuggestionCallback { _, _, suggestion ->
                block().forEach { suggestion.addEntry(SuggestionEntry(it.toString())) }
            }
        }
    }

    inline fun <reified T> argument(
        name: String,
        block: ArgumentBuilder<T>.() -> Unit = {}
    ): Argument<T> {
        val arg: Argument<T> = when (T::class) {
            String::class -> ArgumentType.String(name) as Argument<T>
            Int::class -> ArgumentType.Integer(name) as Argument<T>
            Long::class -> ArgumentType.Long(name) as Argument<T>
            Double::class -> ArgumentType.Double(name) as Argument<T>
            Float::class -> ArgumentType.Float(name) as Argument<T>
            Boolean::class -> ArgumentType.Boolean(name) as Argument<T>
            UUID::class -> ArgumentType.UUID(name) as Argument<T>
            else -> error("Unsupported argument type: ${T::class}. Try using its standalone function (e.g. entityArgument<>())")
        }
        return ArgumentBuilder(arg).apply(block).argument
    }

    fun entityArgument(name: String, block: ArgumentBuilder<*>.() -> Unit = {}) =
        ArgumentBuilder(ArgumentType.Entity(name)).apply(block).argument

    fun blockStateArgument(name: String, block: ArgumentBuilder<*>.() -> Unit = {}) =
        ArgumentBuilder(ArgumentType.BlockState(name)).apply(block).argument

    fun particleArgument(name: String, block: ArgumentBuilder<*>.() -> Unit = {}) =
        ArgumentBuilder(ArgumentType.Particle(name)).apply(block).argument

    fun timeArgument(name: String, block: ArgumentBuilder<*>.() -> Unit = {}) =
        ArgumentBuilder(ArgumentType.Time(name)).apply(block).argument

    fun componentArgument(name: String, block: ArgumentBuilder<*>.() -> Unit = {}) =
        ArgumentBuilder(ArgumentType.Component(name)).apply(block).argument

    fun nbtArgument(name: String, block: ArgumentBuilder<*>.() -> Unit = {}) =
        ArgumentBuilder(ArgumentType.NBT(name)).apply(block).argument

    inline fun <reified E : Enum<E>> enumArgument(
        name: String,
        block: ArgumentBuilder<E>.() -> Unit = {}
    ): Argument<E> {
        val arg = ArgumentType.Enum(name, E::class.java)
        return ArgumentBuilder(arg).apply(block).argument
    }

    inline fun <reified A> syntax(
        argA: Argument<A>,
        crossinline handler: Player.(A) -> Unit
    ) {
        command.addSyntax({ sender, ctx ->
            if (sender is Player) handler(sender, ctx.get(argA))
        }, argA)
    }

    inline fun <reified A, reified B> syntax(
        argA: Argument<A>,
        argB: Argument<B>,
        crossinline handler: Player.(A, B) -> Unit
    ) {
        command.addSyntax({ sender, ctx ->
            if (sender is Player) handler(sender, ctx.get(argA), ctx.get(argB))
        }, argA, argB)
    }

    inline fun <reified A, reified B, reified C> syntax(
        argA: Argument<A>,
        argB: Argument<B>,
        argC: Argument<C>,
        crossinline handler: Player.(A, B, C) -> Unit
    ) {
        command.addSyntax({ sender, ctx ->
            if (sender is Player) handler(sender, ctx.get(argA), ctx.get(argB), ctx.get(argC))
        }, argA, argB, argC)
    }

    fun build(): Command = command
}