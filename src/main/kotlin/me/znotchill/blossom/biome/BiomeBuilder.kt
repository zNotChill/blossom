package me.znotchill.blossom.biome

import net.minestom.server.color.Color
import net.minestom.server.world.biome.Biome
import net.minestom.server.world.biome.BiomeEffects

class BiomeBuilder {
    var temperature: Float = 0f
    var downfall: Float = 0f
    var hasPrecipitation: Boolean = false
    var temperatureModifier: Biome.TemperatureModifier = Biome.TemperatureModifier.NONE
    var effects: BiomeEffects? = null

    fun build(): Biome {
        return Biome.create(
            temperature,
            downfall,
            hasPrecipitation,
            temperatureModifier,
            effects
        )
    }

    fun biomeEffects(block: BiomeEffectsBuilder.() -> Unit): BiomeEffects {
        return BiomeEffectsBuilder()
            .apply(block)
            .build()
    }
}

class BiomeEffectsBuilder {
    var fogColor: Color = Color(0, 0, 0)
    var skyColor: Color = Color(0, 0, 0)
    var waterColor: Color = Color(0, 0, 0)
    var waterFogColor: Color = Color(0, 0, 0)
    var foliageColor: Color = Color(0, 0, 0)
    var grassColor: Color = Color(0, 0, 0)
    var grassColorModifier: BiomeEffects.GrassColorModifier = BiomeEffects.GrassColorModifier.NONE
//    var particle: BiomeEffects.Particle? = null
//    var ambientSound: Sound? = null
//    var moodSound: Sound? = null
//    var additionsSound: Sound? = null
//    var music: WeightedMusic? = null
//    var musicVolume: Float = 1f

    fun build(): BiomeEffects {
        return BiomeEffects.builder()
            .fogColor(fogColor)
            .skyColor(skyColor)
            .waterColor(waterColor)
            .waterFogColor(waterFogColor)
            .foliageColor(foliageColor)
            .grassColor(grassColor)
            .grassColorModifier(grassColorModifier)
            .build()
    }
}