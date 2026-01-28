package me.znotchill.blossom.server.essentials.classes

import me.znotchill.blossom.server.essentials.BlockPicker
import me.znotchill.blossom.server.essentials.BlockPickerConfig
import me.znotchill.blossom.server.essentials.GamemodeSwitcher
import me.znotchill.blossom.server.essentials.GamemodeSwitcherConfig

enum class BareEssential {
    GAMEMODE_SWITCHER,
    BLOCK_PICKER,
    ALL;

    fun create(config: EssentialConfig?): Essential<*> =
        when (this) {
            GAMEMODE_SWITCHER -> GamemodeSwitcher(
                config as? GamemodeSwitcherConfig ?: GamemodeSwitcherConfig()
            )
            BLOCK_PICKER -> BlockPicker(
                config as? BlockPickerConfig ?: BlockPickerConfig()
            )
            ALL -> error("Cannot create ALL instance.")
        }
}

fun BareEssential.withConfig(config: EssentialConfig) =
    BareEssentialEntry.Configured(this, config)

fun BareEssential.asEntry() =
    BareEssentialEntry.Type(this)
