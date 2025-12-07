package xyz.meowing.krypt.utils

import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import xyz.meowing.knit.api.KnitClient.client
import java.awt.Color
import java.util.Optional
import kotlin.math.sqrt

object Utils {
    inline val partialTicks get() = client.deltaTracker.getGameTimeDeltaPartialTick(true)
    private val formatRegex = "[§&][0-9a-fk-or]".toRegex()

    fun Map<*, *>.toColorFromMap(): Color? {
        return try {
            val r = (get("r") as? Number)?.toInt() ?: 255
            val g = (get("g") as? Number)?.toInt() ?: 255
            val b = (get("b") as? Number)?.toInt() ?: 255
            val a = (get("a") as? Number)?.toInt() ?: 255
            Color(r, g, b, a)
        } catch (_: Exception) {
            null
        }
    }

    fun Color.toFloatArray(): FloatArray {
        return floatArrayOf(red / 255f, green / 255f, blue / 255f)
    }

    fun Float.toTimerFormat(decimals: Int = 2): String {
        val hours = (this / 3600).toInt()
        val minutes = ((this % 3600) / 60).toInt()
        val seconds = this % 60f

        val secondsFormatted = String.format("%.${decimals}f", seconds)

        return when {
            hours > 0 -> "${hours}h ${minutes}m ${secondsFormatted}s"
            minutes > 0 -> "${minutes}m ${secondsFormatted}s"
            else -> "${secondsFormatted}s"
        }
    }

    fun String?.removeFormatting(): String {
        if (this == null) return ""
        return this.replace(formatRegex, "")
    }

    fun Any?.equalsOneOf(vararg things: Any?): Boolean {
        return things.any { this == it }
    }

    fun Component.toLegacyString(): String {
        val sb = StringBuilder()
        this.visit({ style, text ->
            style.color?.let { color ->
                ChatFormatting.entries.firstOrNull {
                    it.color == color.value
                }?.let { sb.append('§').append(it.char) }
            }
            if (style.isBold) sb.append("§l")
            if (style.isItalic) sb.append("§o")
            if (style.isUnderlined) sb.append("§n")
            if (style.isStrikethrough) sb.append("§m")
            if (style.isObfuscated) sb.append("§k")
            sb.append(text)
            Optional.empty<Unit>()
        }, Style.EMPTY)
        return sb.toString()
    }

    fun calcDistanceSq(a: BlockPos, b: BlockPos): Double {
        val dx = (a.x - b.x).toDouble()
        val dy = (a.y - b.y).toDouble()
        val dz = (a.z - b.z).toDouble()
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun calcDistance(a: BlockPos, b: BlockPos): Double {
        val dx = (a.x - b.x).toDouble()
        val dy = (a.y - b.y).toDouble()
        val dz = (a.z - b.z).toDouble()
        return dx * dx + dy * dy + dz * dz
    }
}