package me.znotchill.blossom.extensions

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity

fun Pos.distance(entity: Entity): Double {
    return this.distance(entity.position)
}