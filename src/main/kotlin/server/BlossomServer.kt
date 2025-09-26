package me.znotchill.blossom.server

import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandManager
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceManager
import net.minestom.server.timer.SchedulerManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BlossomServer(
    name: String = "Server",
    auth: Boolean = true
) {
    val logger: Logger = LoggerFactory.getLogger(name)
    val authEnum = if (auth) Auth.Online() else Auth.Offline()

    val server: MinecraftServer = MinecraftServer.init(
        authEnum as Auth
    )

    val scheduler: SchedulerManager
        get() = MinecraftServer.getSchedulerManager()

    val eventHandler: GlobalEventHandler
        get() = MinecraftServer.getGlobalEventHandler()

    val instanceManager: InstanceManager
        get() = MinecraftServer.getInstanceManager()

    val players: Collection<Player>
        get() = MinecraftServer.getConnectionManager().onlinePlayers

    val commands: CommandManager
        get() = MinecraftServer.getCommandManager()

    fun registerCommand(command: Command) {
        commands.register(command)
    }

    fun start(
        address: String = "0.0.0.0",
        port: Int = 25565
    ) {
        preLoad()
        server.start(address, port)
        postLoad()
    }

    open fun preLoad() {}
    open fun postLoad() {}

    var brand: String
        get() = MinecraftServer.getBrandName()
        set(value) = MinecraftServer.setBrandName(value)
}