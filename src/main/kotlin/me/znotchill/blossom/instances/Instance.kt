package me.znotchill.blossom.instances

import net.minestom.server.MinecraftServer
import net.minestom.server.instance.DynamicChunk
import net.minestom.server.instance.Instance
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.LightingChunk
import net.minestom.server.instance.generator.GenerationUnit
import net.minestom.server.utils.chunk.ChunkSupplier

interface InstanceTemplate {
    fun new(
        instanceManager: InstanceManager = MinecraftServer.getInstanceManager()
    ): InstanceContainer {
        val instance = instanceManager.createInstanceContainer()

        instance.setGenerator { unit -> generate(unit) }
        instance.chunkSupplier = ChunkSupplier { i, x, z -> chunkSupplier(i, x, z) }

        return instance
    }

    fun generate(unit: GenerationUnit)
    fun chunkSupplier(instance: Instance, x: Int, z: Int): DynamicChunk {
        return LightingChunk(instance, x, z)
    }
}