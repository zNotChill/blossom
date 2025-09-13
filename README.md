
# Blossom

My attempt at making Minestom more Kotlin idiomatic.

## Contents

- [Scheduler](#scheduler)
- [Time](#time)
- [Dialogs](#dialogs)
- [Commands](#commands)
- [Bossbars](#bossbars)
- [Dialogs](#dialogs)
- [Components](#components)

## Install

### Maven
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
<dependency>
    <groupId>com.github.znotchill</groupId>
    <artifactId>blossom</artifactId>
    <version>LATEST</version>
</dependency>
```

### Gradle.kts

```
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.znotchill:blossom:LATEST")
}
```

## Scheduler

Blossom reimplements Minestom's scheduler system in a very basic way.

The `run` block provides you with the `TaskManager` parameter, allowing you to manage the task from inside the task.

#### Tasks are automatically ran.

### Example Server-wide Task

```kt
MinecraftServer.getSchedulerManager().task {
    repeat = 10.ticks
    run = { task ->
        if (MinecraftServer.getConnectionManager().onlinePlayers.isNotEmpty()) {
            task.cancel() // or .stop()
            println("too many players! canceling task!")
        } else {
            println("running task")
        }
    }
}
```

## Time

Blossom provides utilities for managing time very simply.

You can use any number type (float, double, int, long...) to convert a number to a TimeSpan.

Longhand names are exposed as property getters:
- millis
- ticks
- seconds
- minutes
- hours
- days 

Shorthand names are exposed as function getters:
- ms()
- t()
- s()
- m()
- h()
- d()

All of these, no matter shorthand or longhand, return a Time class, containing conversion options, as detailed below.

You can convert a Time class to a Duration class using `Time#duration`.

### Time Example

```kt
val ms = 500.ms()
ms.ticks // returns Time(10, TimeSpan.TICK)
ms.seconds // returns Time(10, TimeSpan.SECOND)

val ms = 500.millis
ms.ticks // returns Time(10, TimeSpan.TICK)
ms.seconds // returns Time(10, TimeSpan.SECOND)

// ALTERNATIVELY, you can choose to convert manually:
val hours = 5.hours
hours.convertTo(TimeSpan.SECOND) // returns Time(18000, TimeSpan.SECOND)
```

## Dialogs

### Example Tab Dialog

```kt
val d = dialog {
    type = DialogType.TABS
    title = Component.text("Upgrade Station")
    defaultTab = "test_tab"

    page(1) {
        tabs {
            tab {
                key = "test_tab"
                label = Component.text("Test Tab")
                activeLabel = Component.text("Test Tab")
                    .color(TextColor.color(52, 180, 235))
                width = 100
                activeWidth = 120

                click = { p, tab, dialog ->
                    println("i have been clicked by ${p.username}!")
                }

                actions {
                    key = "buy_dirt"
                    label = Component.text("Buy Dirt")
                    tooltip = Component.text("300 credits")
                    click = { p, button, dialog ->
                        // credits is a custom extension, 
                        // used for example purposes
                        if (p.credits >= 300) {
                            p.inventory.addItemStack(ItemStack.of(Material.DIRT))
                        }
                    }
                }
            }
        }
    }
}

d.show(player)
```

## Commands

In Blossom, the list of possible arguments are:
- `entityArgument`
- `blockStateArgument`
- `particleArgument`
- `timeArgument`
- `componentArgument`
- `nbtArgument`
- `enumArgument`

Other primitives + UUID are available under the `argument` function.

As of now, this list of `argument` supported types is exhaustive.
```kt
val arg = argument<String>("arg")
val arg = argument<Int>("arg")
val arg = argument<Long>("arg")
val arg = argument<Double>("arg")
val arg = argument<Float>("arg")
val arg = argument<Boolean>("arg")
val arg = argument<UUID>("arg")
```

Each argument function returns a Minestom `ArgumentBuilder` so existing snytax is the same, such as tab completion and default values.

### Gamemode Command (Enum Example)

In default and syntax blocks, the CommandSender is automatically available so you don't need to do `sender.sendMessage` for example.

```kt
command("gamemode") {
    default<ConsoleSender> {
        sendMessage("You must be a player to use this command.")
        return@default
    }

    default<Player> {
        sendMessage("usage: /gamemode <ADVENTURE/CREATIVE/SPECTATOR/SURVIVAL>")
        return@default
    }

    val value1 = enumArgument<GameMode>("gamemode")

    syntax(value1) { value1 ->
        sendMessage("Your gamemode was changed to $value1.")
        gameMode = value1
    }
}
```

# Bossbars

Blossom makes it very easy to create and track bossbars. The tracking is done automatically internally by the library.

### Bossbar Example
```kt
if (!player.hasBossBar("ready_up")) {
    val bar = bossBar("ready_up") {
        text = Component.text("Ready Up!")
        progress = 0.5f
        color = BossBar.Color.BLUE
        overlay = BossBar.Overlay.NOTCHED_20
    }

    bar.show(player)
}
```

Updating the existing bossbar is just as simple:
```kt
val readyUpBar = player.bossBar("ready_up")
readyUpBar?.update {
    progress = 1f
    color = BossBar.Color.PINK
    // ...other properties
}
```

Aswell as hiding a bossbar:
```kt
val readyUpBar = player.bossBar("ready_up")
readyUpBar?.hide()
```

## Components

Blossom makes it simpler and cleaner to use Adventure components.

You can use `space()` inside a component builder to simply add a blank space.

`tooltip()` is a normal text builder.

Inside a `text()` block, you have access to the properties:
- color
- bold
- italic
- underline
- strikethrough
- obfuscate

`onClick` blocks provide you with most click events:
- runCommand(String)
- suggestCommand(String)
- openUrl(String)
- changePage(Int)
- copyToClipboard(String)
- showDialog(DialogLike)

#### Text blocks cannot be chained within one another.

### Component Example

```kt
component {
    text("You got a friend request!")
    space() // literally just adds a blank space
    text("[Accept]") {
        color = Color(255, 255, 85)

        onClick {
            runCommand("friends accept zNotChill")
        }

        tooltip("Click to accept") {
            underline = true
        }
    }
    space()
    text("[Deny]") {
        color = Color(255, 85, 85)

        onClick {
            runCommand("friends deny zNotChill")
        }

        tooltip("Click to deny") {
            underline = true
        }
    }
}
```
