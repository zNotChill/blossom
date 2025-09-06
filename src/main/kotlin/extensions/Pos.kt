package me.znotchill.blossom.extensions

import me.znotchill.blossom.pos.SmallPos
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity

fun Pos.distance(entity: Entity): Double {
    return this.distance(entity.position)
}

fun Pos.toSmall(): SmallPos {
    return SmallPos.fromPos(this)
}