package xyz.meowing.krypt.features.waypoints.utils

import net.minecraft.core.BlockPos

data class StepData(
    val waypoints: MutableList<WaypointData>,
    val line: MutableList<BlockPos>
)