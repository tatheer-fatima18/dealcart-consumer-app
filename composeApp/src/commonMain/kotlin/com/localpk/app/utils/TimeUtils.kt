package com.localpk.app.utils

import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

object TimeUtils {
    fun currentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
    
    fun isTimeStale(lastUpdateTime: Long, maxAgeMinutes: Int): Boolean {
        val now = currentTimeMillis()
        val maxAgeMs = maxAgeMinutes * 60 * 1000L
        return (now - lastUpdateTime) > maxAgeMs
    }
}

fun String?.formatDateWithOne(deliveryDays: Int = RemoteConfigs.getDeliveryDays()): String {
    if (this.isNullOrEmpty()) return EMPTY_STRING
    return try {
        val instant = Instant.parse(this)
        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
        val deliveryDate = localDate.plus(DatePeriod(days = deliveryDays))
        val dayOfMonth = deliveryDate.dayOfMonth
        val month = deliveryDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        "${dayOfMonth}${getDayOfMonthSuffix(dayOfMonth)} $month"
    } catch (e: Exception) {
        EMPTY_STRING
    }
} 

fun String.formatOrderDeliveryDate(): String {
    if (this.isEmpty()) return EMPTY_STRING
    return try {
        val instant = Instant.parse(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val dayOfMonth = localDateTime.dayOfMonth
        val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        "$dayOfMonth${getDayOfMonthSuffix(dayOfMonth)} $month"
    } catch (e: Exception) {
        EMPTY_STRING
    }
}

fun String.month(): String {
    if (isEmpty()) return EMPTY_STRING

    return try {
        val instant = Instant.parse(this)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        dateTime.month.name.take(3)
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        EMPTY_STRING
    }
}


fun String.day(): String {
    if (isEmpty()) return EMPTY_STRING

    return try {
        val instant = Instant.parse(this)
        val localDateTime =
            instant.toLocalDateTime(TimeZone.currentSystemDefault())
        localDateTime.dayOfMonth.toString().padStart(2, '0')
    } catch (e: Exception) {
        EMPTY_STRING
    }
}

fun String.formatMatchDate(): String {
    return month() + " " + day()
}
