package me.znotchill.blossom.extensions

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound
import net.minestom.server.coordinate.Point
import kotlin.collections.forEach

fun Audience.playSounds(sounds: List<Sound>) {
    sounds.forEach {
        this.playSound(it)
    }
}

fun Audience.playSounds(sounds: List<Sound>, emitter: Sound.Emitter) {
    sounds.forEach {
        this.playSound(it, emitter)
    }
}

fun Audience.playSounds(sounds: List<Sound>, point: Point) {
    sounds.forEach {
        // what is this ...
        this.playSound(it, point.x(), point.y(), point.z())
    }
}