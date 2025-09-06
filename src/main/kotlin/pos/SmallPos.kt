package me.znotchill.blossom.pos

import net.minestom.server.coordinate.Pos

data class SmallPos(
    val chunkX: Int,
    val chunkZ: Int,
    val relX: Short,
    val relY: Int,
    val relZ: Short,
) {
    val x: Double
        get() = (chunkX * 16 + relX / 100f).toDouble()

    val y: Double
        get() = (relY / 100f).toDouble()

    val z: Double
        get() = (chunkZ * 16 + relZ / 100f).toDouble()

    fun toPos(): Pos {
        return Pos(
            x,
            y,
            z
        )
    }

    companion object {
        fun fromPos(pos: Pos): SmallPos {
            return SmallPos(
                pos.chunkX(),
                pos.chunkZ(),
                (pos.x % 16).toInt().toShort(),
                pos.y.toInt(),
                (pos.z % 16).toInt().toShort(),
            )
        }
    }
}