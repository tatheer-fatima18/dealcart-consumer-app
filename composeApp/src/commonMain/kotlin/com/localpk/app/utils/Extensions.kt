package com.localpk.app.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.localpk.app.base.ViewState
import com.localpk.app.data.ViewError
import com.localpk.app.firebase.remoteConfig.RemoteConfigs
import com.localpk.app.theming.BorderPrimaryDefault
import com.localpk.app.theming.SurfaceBackground
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> {
    return collectAsState()
}

// Extension functions for ViewState
fun <T> ViewState<T>.isLoading(): Boolean = this is ViewState.Loading
fun <T> ViewState<T>.isSuccess(): Boolean = this is ViewState.Success
fun <T> ViewState<T>.isError(): Boolean = this is ViewState.Error
fun <T> ViewState<T>.isEmpty(): Boolean = this is ViewState.Empty

fun <T> ViewState<T>.getData(): T? = if (this is ViewState.Success) data else null
fun <T> ViewState<T>.getError(): ViewError? = if (this is ViewState.Error) viewError else null

// Extension function for Flow to map ViewState
fun <T, R> Flow<ViewState<T>>.mapViewState(transform: (T) -> R): Flow<ViewState<R>> {
    return map { viewState ->
        when (viewState) {
            is ViewState.Success -> ViewState.Success(transform(viewState.data))
            is ViewState.Error -> ViewState.Error(viewState.viewError)
            is ViewState.Loading -> ViewState.Loading
            is ViewState.Empty -> ViewState.Empty
        }
    }
}

// String extensions
fun String?.orEmpty(): String = this ?: ""
fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()
fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()

// Number extensions
fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0L
fun Double?.orZero(): Double = this ?: 0.0

// Boolean extensions
fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true

// List extensions
fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()
fun <T> List<T>?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

// Safe call extensions
inline fun <T> T?.safeLet(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}

inline fun <T, R> T?.safeLet(block: (T) -> R, default: R): R {
    return if (this != null) {
        block(this)
    } else {
        default
    }
}

// Collection extensions
fun <T> Collection<T>?.sizeOrZero(): Int = this?.size ?: 0

// Map extensions
fun <K, V> Map<K, V>?.orEmpty(): Map<K, V> = this ?: emptyMap()
fun <K, V> Map<K, V>?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

// Formatting extensions
fun String.capitalizeFirst(): String {
    return if (isNotEmpty()) {
        this[0].uppercase() + substring(1)
    } else {
        this
    }
}

fun String.toTitleCase(): String {
    return split(" ").joinToString(" ") { it.capitalizeFirst() }
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun Int?.isZero() = this.isNotNull() && this == 0

fun Int?.isOne() = this.isNotNull() && this == 1

fun Double.toInteger() = this.toInt()

fun Int?.isNotZero() = this.isNotNull() && !this.isZero()

fun Int?.isPositiveNumber() = (this != null) && (this >= 1)

fun Int?.getValueOrDefault(default: Int = 0) = this ?: default

fun Double?.getValueOrDefault(default: Double = 0.0) = this ?: default

fun Long?.getValueOrDefault(default: Long = 0L) = this ?: default

fun Int.isOne() = this == 1

fun Int.isNotOne() = this != 1

fun Double.isZero() = this == 0.0

fun Double.isNotZero() = this.isNotNull() && !this.isZero()

fun Long?.isZero() = this.isNotNull() && this == 0L

fun Long?.isNotZero() = this.isNotNull() && this != 0L


fun String?.toIntOrZero(): Int {
    return if (this.isNullOrEmpty()) 0 else if (this.contains("null")) 0 else this.toInt()
}

fun Int.convertSingleToTwoDigit(): String {
    return if (this < 9) "0${this}" else "$this"
}

fun Modifier.showIf(condition: Boolean): Modifier {
    return if (condition) this else Modifier.size(0.dp)
}

fun Double.formatPKRCurrency(): String {
    val formatted = buildString {
        val parts = this@formatPKRCurrency.toLong().toString().reversed().chunked(3)
        append(parts.joinToString(",").reversed())
    }
    return "Rs. $formatted"
}

fun Double.formatNumber(): String {
    val formatted = buildString {
        val parts = this@formatNumber.toLong().toString().reversed().chunked(3)
        append(parts.joinToString(",").reversed())
    }
    return formatted
}

fun Double.formatToTwoDecimalString(): String {
    val rounded = (this * 100.0).roundToInt() / 100.0
    val parts = rounded.toString().split(".")
    return if (parts.size == 2) {
        val decimalPart = parts[1].padEnd(2, '0').take(2)
        "${parts[0]}.$decimalPart"
    } else {
        "${parts[0]}.00"
    }
}

fun Float.formatToTwoDecimalString(): String {
    val rounded = (this * 100.0).roundToInt() / 100.0
    val parts = rounded.toString().split(".")
    return if (parts.size == 2) {
        val decimalPart = parts[1].padEnd(2, '0').take(2)
        "${parts[0]}.$decimalPart"
    } else {
        "${parts[0]}.00"
    }
}

fun convertTo12HourFormat(time: String): String {
    return try {
        val localTime = LocalTime.parse(time) // expects format like "14:30"
        val hour = localTime.hour % 12
        val displayHour = if (hour == 0) 12 else hour
        val minute = localTime.minute.toString().padStart(2, '0')
        val amPm = if (localTime.hour < 12) "AM" else "PM"
        "$displayHour:$minute $amPm"
    } catch (e: Exception) {
        ""
    }
}


fun Long.formatDateTime(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val time = instant.toLocalDateTime(TimeZone.UTC).time

    val hours = time.hour.toString().padStart(2, '0')
    val minutes = time.minute.toString().padStart(2, '0')
    val seconds = time.second.toString().padStart(2, '0')

    return "${hours}h ${minutes}m ${seconds}s"
}

fun <T> List<T>?.orEmptyArrayList(): ArrayList<T> = this as? ArrayList<T> ?: arrayListOf()

fun Modifier.shimmerEffect(
    visible: Boolean = true,
    shimmerColor: Color = BorderPrimaryDefault,
    backgroundColor: Color = SurfaceBackground,
    cornerRadius: Dp = 8.dp,
    durationMillis: Int = 1000
): Modifier = if (visible) composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )

    val brush = Brush.linearGradient(
        colors = listOf(backgroundColor, shimmerColor, backgroundColor),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )

    this
        .clip(RoundedCornerShape(cornerRadius))
        .background(brush)
} else this

fun String?.formatConfirmTimelineDate(): String {
    if (this.isNullOrEmpty()) return EMPTY_STRING

    return try {
        val instant = Instant.parse(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val year = localDateTime.year.toString()

        "$day $month $year"
    } catch (e: Exception) {
        ""
    }
}


fun String?.formatConfirmTimelineTime(): String {
    if (this.isNullOrEmpty()) return EMPTY_STRING

    return try {
        val instant = Instant.parse(this)
        val localTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time

        val hour = localTime.hour % 12
        val displayHour = if (hour == 0) 12 else hour
        val minute = localTime.minute.toString().padStart(2, '0')
        val amPm = if (localTime.hour < 12) "AM" else "PM"

        "$displayHour:$minute $amPm"
    } catch (e: Exception) {
        ""
    }
}

fun Long.hour(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.UTC)
    return dateTime.hour.toString().padStart(2, '0')
}

fun Long.minutes(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.UTC)
    return dateTime.minute.toString().padStart(2, '0')
}

fun Long.seconds(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime = instant.toLocalDateTime(TimeZone.UTC)
    return dateTime.second.toString().padStart(2, '0')
}

fun String.formatDate(): String {
    if (this.isEmpty()) return EMPTY_STRING

    return try {
        val instant = Instant.parse(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        "${
            localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        } ${localDateTime.dayOfMonth}, ${localDateTime.year}"
    } catch (e: Exception) {
        ""
    }
}


fun getDeliveryDate(): String {

    // Get current date in system's local timezone
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val deliveryDate = currentDate.plus(DatePeriod(days = RemoteConfigs.getDeliveryDays()))

    val day = deliveryDate.dayOfMonth
    val month = deliveryDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)

    return "$day${getDayOfMonthSuffix(day)} $month"
}

fun getDayOfMonthSuffix(day: Int): String {
    if (day in 11..13) {
        return "th"
    }
    return when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

fun String.getQueryParameter(param: String): String? {
    val query = this.substringAfter("?", "")
    return query.split("&")
        .mapNotNull {
            val (key, value) = it.split("=")
                .let { parts -> parts.getOrNull(0) to parts.getOrNull(1) }
            if (key == param) value else null
        }
        .firstOrNull()
}

fun String?.formatCartDeliveryDate(showFullMonth: Boolean = false): String {
    if (this.isNullOrEmpty()) return EMPTY_STRING

    return try {
        val localDate = try {
            Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault()).date
        } catch (_: Exception) {
            LocalDate.parse(this)
        }

        val day = localDate.dayOfMonth
        val month = if (showFullMonth) {
            localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
        } else {
            localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        }
        "$day $month ${localDate.year}"
    } catch (e: Exception) {
        ""
    }
}

fun Long.formatMillisToDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.year}/${
        localDateTime.monthNumber.toString().padStart(2, '0')
    }/${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
}

fun String.formatWithCommas(): String {
    return this
        .replace(",", "")
        .toDoubleOrNull()
        ?.let { number ->
            val parts =
                number.toLong().toString().reversed().chunked(3).joinToString(",").reversed()
            if (number % 1 == 0.0) {
                parts
            } else {
                val decimalPart = number.toString().substringAfter(".")
                "$parts.$decimalPart"
            }
        } ?: this
}

fun formatPhoneNumber(phoneNumber: String?): String? {
    return phoneNumber?.let {
        var formattedNumber = it.replace("\\D".toRegex(), "")
        if (formattedNumber.startsWith("92")) {
            formattedNumber = formattedNumber.substring(2)
        } else if (formattedNumber.startsWith("0")) {
            formattedNumber = formattedNumber.substring(1)
        }
        formattedNumber
    }
}

fun formatOvers(overs: List<Long>?): String {
    if (overs.isNullOrEmpty()) {
        return ZERO_STRING
    }

    val bowlerOvers = overs.first()
    val balls = overs.last()

    return if (balls == ZERO_LONG) {
        "$bowlerOvers"
    } else {
        "$bowlerOvers.$balls"
    }
}



