package xyz.meowing.krypt.features.waypoints.utils

import xyz.meowing.krypt.features.waypoints.DungeonWaypoints
import java.awt.Color

enum class WaypointType {
    START,
    SECRET,
    BAT,
    MINE,
    LEVER,
    SUPERBOOM,
    ETHERWARP,
    CUSTOM,
    ;

    val color: Color
        get() = when (this) {
            BAT -> DungeonWaypoints.batColor
            MINE -> DungeonWaypoints.mineColor
            SECRET -> DungeonWaypoints.secretColor
            ETHERWARP -> DungeonWaypoints.etherWarpColor
            SUPERBOOM -> DungeonWaypoints.superBoomColor
            LEVER -> DungeonWaypoints.leverColor
            START -> DungeonWaypoints.startColor
            CUSTOM -> Color.WHITE
        }


    val label: String
        get() = when (this) {
            BAT -> "Bat"
            MINE -> "Mine"
            SECRET -> "Click"
            ETHERWARP -> "Warp"
            SUPERBOOM -> "Boom!"
            LEVER -> "Flick"
            START -> ""
            CUSTOM -> name
        }

    companion object {
        fun fromString(value: String?): WaypointType? {
            if (value == null) return null
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}