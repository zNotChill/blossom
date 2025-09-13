package me.znotchill.blossom.scheduler

import net.minestom.server.timer.SchedulerManager

fun SchedulerManager.task(block: SchedulerBuilder.() -> Unit): SchedulerBuilder {
    val builder = SchedulerBuilder(this).apply(block)
    builder.build()
    return builder
}