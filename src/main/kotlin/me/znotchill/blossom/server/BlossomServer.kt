package me.znotchill.blossom.server

import me.znotchill.blossom.server.essentials.classes.Essential
import me.znotchill.blossom.server.essentials.classes.BareEssential
import me.znotchill.blossom.server.essentials.classes.BareEssentialEntry
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.advancements.AdvancementManager
import net.minestom.server.command.CommandManager
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.block.banner.BannerPattern
import net.minestom.server.recipe.RecipeManager
import net.minestom.server.registry.DynamicRegistry
import net.minestom.server.scoreboard.TeamManager
import net.minestom.server.timer.SchedulerManager
import net.minestom.server.world.biome.Biome
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BlossomServer(
    name: String = "Server",
    auth: Boolean = true,
    useBareEssentials: Boolean = false,
    bareEssentials: List<BareEssentialEntry> = listOf()
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

    val biomes: DynamicRegistry<Biome> = MinecraftServer.getBiomeRegistry()
    val advancements: AdvancementManager = MinecraftServer.getAdvancementManager()
    val bannerPatterns: DynamicRegistry<BannerPattern> = MinecraftServer.getBannerPatternRegistry()
    val bossbars: net.minestom.server.adventure.bossbar.BossBarManager = MinecraftServer.getBossBarManager()
    val teams: TeamManager = MinecraftServer.getTeamManager()
    val recipes: RecipeManager = MinecraftServer.getRecipeManager()

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

    /**
     * Essentials manager
     */
    private val essentials = mutableListOf<Essential<*>>()

    init {
        if (useBareEssentials) {
            loadEssentials(bareEssentials)
        }
    }

    private fun loadEssentials(entries: List<BareEssentialEntry>) {
        val resolved = resolveAll(entries)

        resolved.forEach { entry ->
            when (entry) {
                is BareEssentialEntry.Type -> {
                    val essential = entry.type.create(null)
                    essential.load(this)
                    essentials += essential
                }

                is BareEssentialEntry.Configured -> {
                    val essential = entry.type.create(entry.config)
                    essential.load(this)
                    essentials += essential
                }
            }
        }
    }

    private fun resolveAll(entries: List<BareEssentialEntry>): List<BareEssentialEntry> {
        val hasAll = entries.any {
            it is BareEssentialEntry.Type && it.type == BareEssential.ALL
        }

        return if (!hasAll) entries else {
            BareEssential.entries
                .filter { it != BareEssential.ALL }
                .map { BareEssentialEntry.Type(it) }
        }
    }
}