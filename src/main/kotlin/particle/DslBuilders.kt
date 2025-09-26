package me.znotchill.blossom.particle

fun particle(
    block: ParticleBuilder.() -> Unit = {}
): ParticleBuilder {
    val builder = ParticleBuilder().apply(block)
    return builder
}