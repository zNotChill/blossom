package me.znotchill.blossom.biome

import net.minestom.server.color.Color
import net.minestom.server.sound.SoundEvent
import net.minestom.server.world.attribute.AmbientSounds
import net.minestom.server.world.attribute.EnvironmentAttribute
import net.minestom.server.world.attribute.EnvironmentAttributeMap
import net.minestom.server.world.biome.Biome
import net.minestom.server.world.biome.BiomeEffects

class BiomeBuilder {
    var temperature: Float = 0.8f
    var downfall: Float = 0.4f
    var hasPrecipitation: Boolean = true
    var temperatureModifier: Biome.TemperatureModifier = Biome.TemperatureModifier.NONE

    var effects: BiomeEffects = BiomeEffects.DEFAULT

    private val attributeBuilder = EnvironmentAttributeMap.builder()

    /**
     * Directly set an environment attribute value
     */
    fun <T> attribute(attr: EnvironmentAttribute<T>, value: T) {
        attributeBuilder.set(attr, value)
    }

    /**
     * Apply a modifier to an environment attribute
     */
    fun <T, Arg> modifyAttribute(
        attr: EnvironmentAttribute<T>,
        modifier: EnvironmentAttribute.Modifier<T, Arg>,
        argument: Arg
    ) {
        attributeBuilder.modify(attr, modifier, argument)
    }

    fun build(): Biome {
        return Biome.create(
            hasPrecipitation,
            temperature,
            temperatureModifier,
            downfall,
            attributeBuilder.build(),
            effects
        )
    }

    fun biomeEffects(block: BiomeEffectsBuilder.() -> Unit): BiomeEffects {
        return BiomeEffectsBuilder()
            .apply(block)
            .build()
    }

    fun ambientSounds(
        loop: SoundEvent? = null,
        mood: AmbientSounds.Mood? = null,
        additions: List<AmbientSounds.Additions> = emptyList()
    ) {
        val ambient = AmbientSounds(
            loop,
            mood,
            additions
        )
        this.attribute(EnvironmentAttribute.AMBIENT_SOUNDS, ambient)
    }
}

class BiomeEffectsBuilder {
    var waterColor: Color = Color(0x3f76e4)
    var foliageColor: Color? = null
    var dryFoliageColor: Color? = null
    var grassColor: Color? = null
    var grassColorModifier: BiomeEffects.GrassColorModifier = BiomeEffects.GrassColorModifier.NONE

    fun build(): BiomeEffects {
        return BiomeEffects.builder()
            .waterColor(waterColor)
            .foliageColor(foliageColor)
            .dryFoliageColor(dryFoliageColor)
            .grassColor(grassColor)
            .grassColorModifier(grassColorModifier)
            .build()
    }
}

class MoodSoundBuilder {
    var sound: SoundEvent = SoundEvent.AMBIENT_CAVE
    var tickDelay: Int = 6000
    var blockSearchExtent: Int = 8
    var offset: Double = 2.0

    fun build() = AmbientSounds.Mood(sound, tickDelay, blockSearchExtent, offset)
}

class AdditionsSoundBuilder {
    var sound: SoundEvent = SoundEvent.AMBIENT_CAVE
    var tickChance: Double = 0.01

    fun build() = AmbientSounds.Additions(sound, tickChance)
}