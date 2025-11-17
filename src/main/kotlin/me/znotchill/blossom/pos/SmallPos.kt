package me.znotchill.blossom.pos

import net.minestom.server.coordinate.Pos

data class SmallPos(
    val chunkX: Int,
    val chunkZ: Int,
    val relX: Short,
    val relY: Double,
    val relZ: Short,
) {
    val x: Double
        get() = (chunkX * 16) + relX / 100.0

    val y: Double
        get() = relY

    val z: Double
        get() = (chunkZ * 16) + relZ / 100.0

    fun toPos(): Pos = Pos(x, y, z)

    companion object {
        fun fromPos(pos: Pos): SmallPos {
            val chunkX = pos.chunkX()
            val chunkZ = pos.chunkZ()

            val relX = ((pos.x - chunkX * 16) * 100).toInt().toShort()
            val relZ = ((pos.z - chunkZ * 16) * 100).toInt().toShort()

            return SmallPos(
                chunkX,
                chunkZ,
                relX,
                pos.y,
                relZ
            )
        }
    }
}
