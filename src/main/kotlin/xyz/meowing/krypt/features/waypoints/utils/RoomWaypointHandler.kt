package xyz.meowing.krypt.features.waypoints.utils

import net.minecraft.core.BlockPos
import xyz.meowing.krypt.api.dungeons.enums.map.Room
import xyz.meowing.krypt.api.dungeons.enums.map.RoomRotations

object RoomWaypointHandler {
    /*
    private val roomWaypoints = mutableMapOf<String, MutableList<Waypoint>>()

    fun loadWaypointsForRoom(room: Room) {
        val roomName = room.name ?: return
        if (roomWaypoints.containsKey(roomName)) return

        val waypointData = WaypointRegistry.getWaypointsForRoom(roomName) ?: return

        val waypoints = waypointData
            .map { it.toWaypoint(room.getRealCoord(BlockPos(it.x, it.y, it.z))) }
            .toMutableList()

        roomWaypoints[roomName] = waypoints
    }

    fun getWaypoints(room: Room): List<Waypoint>? {
        val roomName = room.name ?: return null
        return roomWaypoints[roomName]
    }

    fun clear() {
        roomWaypoints.clear()
    }

    fun updateWaypoints(roomName: String, waypoints: MutableList<Waypoint>) {
        roomWaypoints[roomName] = waypoints
    }

    fun reloadCurrentRoom() {
        val room = DungeonAPI.currentRoom ?: return
        val roomName = room.name ?: return

        roomWaypoints.remove(roomName)
        loadWaypointsForRoom(room)
    }

     */
}