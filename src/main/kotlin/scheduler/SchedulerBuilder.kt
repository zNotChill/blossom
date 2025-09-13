package me.znotchill.blossom.scheduler

import me.znotchill.blossom.extensions.Time
import net.minestom.server.timer.SchedulerManager
import net.minestom.server.timer.Task

class SchedulerBuilder(
    val scheduler: SchedulerManager
) {
    var delay: Time? = null
    var repeat: Time? = null
    var run: ((taskManager: TaskManager) -> Unit)? = null

    fun build(): Task {
        val action = run ?: error("No run block provided for scheduled task")

        lateinit var scheduledTask: Task

        val taskBuilder = scheduler.buildTask {
            action(TaskManager(scheduledTask))
        }

        delay?.let { taskBuilder.delay(delay!!.duration) }
        repeat?.let { taskBuilder.repeat(repeat!!.duration) }

        scheduledTask = taskBuilder.schedule()
        return scheduledTask
    }
}

class TaskManager(
    val task: Task
) {
    fun stop() {
        task.cancel()
    }

    fun cancel() {
        task.cancel()
    }
}