package me.znotchill.blossom.extensions

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

enum class TimeSpan(
    val millisPerUnit: Long,
    val shortHand: String,
    val temporalUnit: TemporalUnit
) {
    MILLISECOND(1, "ms", ChronoUnit.MILLIS),
    TICK(50, "ticks", ChronoUnit.MILLIS),
    SECOND(1000, "s", ChronoUnit.SECONDS),
    MINUTE(60_000, "m", ChronoUnit.MINUTES),
    HOUR(3_600_000, "h", ChronoUnit.HOURS),
    DAY(86_400_000, "d", ChronoUnit.DAYS);

    fun toMillis(amount: Long): Long = amount * millisPerUnit
    fun fromMillis(millis: Long): Long = millis / millisPerUnit
}

class Time(
    val time: Long,
    val span: TimeSpan
) {
    fun convertTo(target: TimeSpan): Time {
        val millis = span.toMillis(time)
        val newAmount = target.fromMillis(millis)
        return Time(newAmount, target)
    }

    val ticks: Time get() = convertTo(TimeSpan.TICK)
    val millis: Time get() = convertTo(TimeSpan.MILLISECOND)
    val seconds: Time get() = convertTo(TimeSpan.SECOND)
    val minutes: Time get() = convertTo(TimeSpan.MINUTE)
    val hours: Time get() = convertTo(TimeSpan.HOUR)
    val days: Time get() = convertTo(TimeSpan.DAY)

    val duration: Duration
        get() {
            return Duration.of(time, span.temporalUnit)
        }

    override fun toString(): String = "$time${span.shortHand}"
}

val Number.millis get() = Time(this.toLong(), TimeSpan.MILLISECOND)
val Number.ticks get() = Time(this.toLong(), TimeSpan.TICK)
val Number.seconds get() = Time(this.toLong(), TimeSpan.SECOND)
val Number.minutes get() = Time(this.toLong(), TimeSpan.MINUTE)
val Number.hours get() = Time(this.toLong(), TimeSpan.HOUR)
val Number.days get() = Time(this.toLong(), TimeSpan.DAY)

fun Number.ms() = this.millis
fun Number.t() = this.ticks
fun Number.s() = this.seconds
fun Number.m() = this.minutes
fun Number.h() = this.hours
fun Number.d() = this.days