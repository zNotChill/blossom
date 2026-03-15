package me.znotchill.blossom.scheduler

import net.minestom.server.timer.Scheduler

fun Scheduler.task(block: SchedulerBuilder.() -> Unit): SchedulerBuilder {
    val builder = SchedulerBuilder(this).apply(block)
    builder.build()
    return builder
}