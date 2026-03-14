package me.znotchill.blossom.biome

import net.minestom.server.color.Color
import net.minestom.server.sound.SoundEvent
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
    var foliageColor: Color? = null
    var grassColor: Color? = null
    var grassColorModifier: BiomeEffects.GrassColorModifier = BiomeEffects.GrassColorModifier.NONE
    var particle: BiomeEffects.Particle? = null
    var ambientSound: SoundEvent? = null
    var moodSound: BiomeEffects.MoodSound? = null
    var additionsSound: BiomeEffects.AdditionsSound? = null
    var music: MutableList<BiomeEffects.WeightedMusic> = mutableListOf()
    var musicVolume: Float? = null

    fun moodSound(block: MoodSoundBuilder.() -> Unit) {
        moodSound = MoodSoundBuilder().apply(block).build()
    }

    fun additionsSound(block: AdditionsSoundBuilder.() -> Unit) {
        additionsSound = AdditionsSoundBuilder().apply(block).build()
    }

    fun music(music: net.minestom.server.sound.Music, weight: Int = 1) {
        this.music.add(BiomeEffects.WeightedMusic(music, weight))
    }

    fun build(): BiomeEffects {
        return BiomeEffects.builder()
            .fogColor(fogColor)
            .skyColor(skyColor)
            .waterColor(waterColor)
            .waterFogColor(waterFogColor)
            .foliageColor(foliageColor)
            .grassColor(grassColor)
            .grassColorModifier(grassColorModifier)
            .biomeParticle(particle)
            .ambientSound(ambientSound)
            .moodSound(moodSound)
            .additionsSound(additionsSound)
            .music(if (music.isEmpty()) null else music)
            .musicVolume(musicVolume)
            .build()
    }
}

class MoodSoundBuilder {
    var sound: SoundEvent = SoundEvent.AMBIENT_CAVE
    var tickDelay: Int = 6000
    var blockSearchExtent: Int = 8
    var offset: Double = 2.0

    fun build() = BiomeEffects.MoodSound(sound, tickDelay, blockSearchExtent, offset)
}

class AdditionsSoundBuilder {
    var sound: SoundEvent = SoundEvent.AMBIENT_CAVE
    var tickChance: Double = 0.01

    fun build() = BiomeEffects.AdditionsSound(sound, tickChance)
}