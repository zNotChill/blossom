package me.znotchill.blossom.server

import me.znotchill.blossom.server.essentials.classes.Essential
import net.minestom.server.Auth
import net.minestom.server.MinecraftServer
import net.minestom.server.advancements.AdvancementManager
import net.minestom.server.command.CommandManager
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
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

/**
 * Blossom's core wrapper around Minestom's [MinecraftServer].
 *
 * Subclass this and override [preLoad] / [postLoad] to set up your server:
 *
 * ```kt
 * class MyServer : BlossomServer("MyServer") {
 *     override fun preLoad() {
 *         brand = "My Awesome Server"
 *         bareEssential<SpawnEssential> { ... }
 *     }
 * }
 * ```
 *
 * @param name Used as the [logger] name and displayed in log output.
 * @param auth Authentication mode. Defaults to [Auth.Online].
 */
open class BlossomServer(
    name: String = "Server",
    auth: Auth = Auth.Online()
) {

    /** SLF4J logger scoped to this server instance. */
    val logger: Logger = LoggerFactory.getLogger(name)

    /**
     * The underlying Minestom server instance.
     * Initialised immediately.
     */
    val server: MinecraftServer = MinecraftServer.init(
        auth
    )

    /**
     * The server brand string sent to clients (visible in F3).
     * Can be changed any time before or after [start].
     */
    var brand: String
        get() = MinecraftServer.getBrandName()
        set(value) = MinecraftServer.setBrandName(value)

    //
    // Minestom Managers
    //

    val scheduler: SchedulerManager = MinecraftServer.getSchedulerManager()
    val eventHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()

    val instanceManager: InstanceManager = MinecraftServer.getInstanceManager()

    val commands: CommandManager = MinecraftServer.getCommandManager()

    //
    // Minestom Registries/other managers
    //
    val biomes: DynamicRegistry<Biome> = MinecraftServer.getBiomeRegistry()
    val advancements: AdvancementManager = MinecraftServer.getAdvancementManager()
    val bannerPatterns: DynamicRegistry<BannerPattern> = MinecraftServer.getBannerPatternRegistry()
    val bossbars: net.minestom.server.adventure.bossbar.BossBarManager = MinecraftServer.getBossBarManager()
    val teams: TeamManager = MinecraftServer.getTeamManager()
    val recipes: RecipeManager = MinecraftServer.getRecipeManager()

    /** All players currently connected to the server. */
    val players: Collection<Player>
        get() = MinecraftServer.getConnectionManager().onlinePlayers

    fun registerCommand(command: Command) {
        commands.register(command)
    }

    //
    // Server Lifecycle
    //

    /**
     * Starts the server on the given [address] and [port].
     *
     * Order:
     * 1. [preLoad]: runs before the socket binds (register essentials/commands here)
     * 2. Server starts through Minestom as normal
     * 3. [postLoad]: runs after starting the Minestom server
     *
     * @param address The bind address. Defaults to `"0.0.0.0"`.
     * @param port The port to listen on. Defaults to `25565`.
     */
    fun start(
        address: String = "0.0.0.0",
        port: Int = 25565
    ) {
        preLoad()
        server.start(address, port)
        postLoad()
    }

    /**
     * Called before the server starts.
     * Override to register commands, essentials, event listeners, etc.
     */
    open fun preLoad() {}

    /**
     * Called after the server starts and is accepting connections.
     * Calls [loadEssentials] by default. Use `super.postLoad()` if you override this.
     */
    open fun postLoad() {
        loadEssentials()
    }

    /**
     * Registers a JVM shutdown hook.
     * Useful for saving data or gracefully disconnecting players when the process exits.
     *
     * ```kotlin
     * onShutdown { database.close() }
     * ```
     */
    fun onShutdown(block: () -> Unit) {
        Runtime.getRuntime().addShutdownHook(Thread(block))
    }

    //
    // Essentials
    //

    /**
     * All registered [Essential] instances.
     * Essentials are loaded during [postLoad] via [loadEssentials].
     */
    internal val essentials = mutableListOf<Essential<*>>()

    /**
     * Adds an [essential] to the registry.
     * Duplicate instances (by object identity) are silently ignored.
     *
     * Use [bareEssential] for essentials that need configuration,
     * or [addEssential] when you already hold a constructed instance.
     */
    fun addEssential(essential: Essential<*>) {
        if (essentials.contains(essential)) return
        essentials.add(essential)
    }

    /**
     * Constructs an [Essential] of type [T],
     * applies [block] for configuration, then registers it.
     *
     * ```kotlin
     * bareEssential<SpawnEssential> {
     *     spawnLocation = Pos(0.0, 64.0, 0.0)
     * }
     * ```
     *
     * @throws NoSuchMethodException if [T] does not have a no-arg constructor (or reflection fails).
     */
    inline fun <reified T : Essential<*>> bareEssential(
        noinline block: T.() -> Unit
    ) {
        val instance = T::class.java.getDeclaredConstructor().newInstance()
        instance.block()

        addEssential(instance)
    }

    /**
     * Loads all registered essentials by calling [Essential.load] on each.
     * Snapshots the list before iterating to avoid potential [java.util.ConcurrentModificationException]s.
     */
    private fun loadEssentials() {
        val currentEssentials = essentials.toList()

        currentEssentials.forEach {
            it.load(this)
        }
    }

    //
    // Events
    //

    /**
     * Registers a listener for events of type [T] that fires exactly once, then unregisters itself.
     *
     * ```kotlin
     * once<PlayerLoginEvent> { event ->
     *     event.player.sendMessage("${event.player.username} joined.")
     * }
     * ```
     */
    inline fun <reified T : Event> once(noinline callback: (T) -> Unit) {
        var node: EventNode<Event>? = null
        node = EventNode.all("once-${T::class.simpleName}").also { n ->
            n.addListener(T::class.java) { event ->
                callback(event)
                eventHandler.removeChild(n)
            }
            eventHandler.addChild(n)
        }
    }

    /**
     * Registers a session long listener for events of type [T].
     *
     * The listener remains active for the lifetime of the server.
     * For a one time listener, use [once] instead.
     *
     * ```kotlin
     * listener<PlayerLoginEvent> { event ->
     *     event.player.sendMessage("${event.player.username} joined.")
     * }
     * ```
     */
    inline fun <reified T : Event> BlossomServer.listener(
        noinline callback: (T) -> Unit
    ) {
        this.eventHandler.addListener(T::class.java) { event ->
            callback(event)
        }
    }
}