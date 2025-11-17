package me.znotchill.blossom.biome

import net.minestom.server.world.biome.Biome

fun biome(block: BiomeBuilder.() -> Unit): Biome {
    return BiomeBuilder()
        .apply(block)
        .build()
}