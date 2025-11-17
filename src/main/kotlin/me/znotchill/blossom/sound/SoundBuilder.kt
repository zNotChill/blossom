package me.znotchill.blossom.sound

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

class SoundBuilder(
    val name: Key
) {
    var source: Sound.Source = Sound.Source.MASTER
    var volume: Double = 1.0
    var pitch: Double = 1.0

    fun build(): Sound {
        return Sound.sound(
            name,
            source,
            volume.toFloat(),
            pitch.toFloat()
        )
    }
}