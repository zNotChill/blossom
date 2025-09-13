
# Blossom

My attempt at making Minestom more Kotlin idiomatic.

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
