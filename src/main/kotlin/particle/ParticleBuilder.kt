package me.znotchill.blossom.particle

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle

class ParticleBuilder {
    var particle: Particle = Particle.FLASH
    var offset: Vec? = Vec(0.0, 0.0, 0.0)
    var count: Int = 1
    var speed: Float = 1f

    fun toPacket(location: Pos): ParticlePacket {
        return ParticlePacket(
            particle,
            location,
            offset,
            speed,
            count
        )
    }
}