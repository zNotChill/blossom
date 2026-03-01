package me.znotchill.blossom.server.essentials

import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.blossom.server.essentials.classes.Essential
import me.znotchill.blossom.server.essentials.classes.EssentialConfig
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.attribute.Attribute
import net.minestom.server.event.player.PlayerPickBlockEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import kotlin.math.roundToInt

data class BlockPickerConfig(
    var gameModes: List<GameMode> = listOf(GameMode.CREATIVE)
) : EssentialConfig

class BlockPicker(
    override val config: BlockPickerConfig = BlockPickerConfig()
) : Essential<BlockPickerConfig>,
    EssentialConfig by config
{
    override fun load(server: BlossomServer) {
        server.eventHandler.addListener<PlayerPickBlockEvent> { event ->
            if (config.gameModes.contains(event.player.gameMode)) {
                val target = event.player.getTargetBlockPosition(
                    event.player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE).value.roundToInt()
                )
                val block = event.player.instance.getBlock(target)

                val mat = Material.fromKey(block.name())
                val item = ItemStack.builder(mat).amount(1).build()

                val slot = event.player.heldSlot
                event.player.inventory.setItemStack(slot.toInt(), item)
            }
        }
    }
}