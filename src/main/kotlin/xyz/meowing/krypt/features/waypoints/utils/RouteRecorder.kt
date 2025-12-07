package xyz.meowing.krypt.features.waypoints.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.sounds.SoundEvents
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import xyz.meowing.knit.api.KnitChat
import xyz.meowing.knit.api.KnitClient
import xyz.meowing.knit.api.KnitPlayer
import xyz.meowing.krypt.annotations.Module
import xyz.meowing.krypt.api.dungeons.DungeonAPI
import xyz.meowing.krypt.api.dungeons.enums.map.Room
import xyz.meowing.krypt.api.dungeons.enums.map.RoomRotations
import xyz.meowing.krypt.api.location.SkyBlockIsland
import xyz.meowing.krypt.events.EventBus
import xyz.meowing.krypt.events.core.DungeonEvent
import xyz.meowing.krypt.events.core.RenderEvent
import xyz.meowing.krypt.events.core.SoundEvent
import xyz.meowing.krypt.events.core.TickEvent
import xyz.meowing.krypt.utils.Utils.calcDistanceSq
import xyz.meowing.krypt.utils.modMessage

@Module
object RouteRecorder {
    var recording = false
        private set

    private val route = mutableListOf<StepData>()
    private var stepIndex = 0
    private var currentRoomName: String? = null
    private var lastPlayerPos: BlockPos? = null

    val currentStep: StepData get() = route[stepIndex]
    val lastStep: StepData get() = route[stepIndex - 1]

    init {
        EventBus.registerIn<DungeonEvent.Room.Change>(SkyBlockIsland.THE_CATACOMBS) {
            if (!recording) return@registerIn

            KnitChat.modMessage("§cError: left room, stopping")
            stopRecording()
        }

        EventBus.registerIn<DungeonEvent.Secrets.Bat>(SkyBlockIsland.THE_CATACOMBS) { addWaypoint(WaypointType.SECRET, it.entity.blockPosition()) }
        EventBus.registerIn<DungeonEvent.Secrets.Chest>(SkyBlockIsland.THE_CATACOMBS) { addWaypoint(WaypointType.SECRET, it.blockPos) }
        EventBus.registerIn<DungeonEvent.Secrets.Essence>(SkyBlockIsland.THE_CATACOMBS) { addWaypoint(WaypointType.SECRET, it.blockPos)}

        EventBus.registerIn<DungeonEvent.Secrets.Item>(SkyBlockIsland.THE_CATACOMBS) {
            val pos = KnitClient.world?.getEntity(it.entityId)?.blockPosition() ?: return@registerIn
        }


        EventBus.registerIn<DungeonEvent.Secrets.Misc>(SkyBlockIsland.THE_CATACOMBS) {
            when (it.secretType) {
                DungeonEvent.Secrets.Type.RED_SKULL -> { /*TODO*/ }
                DungeonEvent.Secrets.Type.LEVER -> { addWaypoint(WaypointType.LEVER, it.blockPos) }
            }
        }

        EventBus.registerIn<SoundEvent.Play>(SkyBlockIsland.THE_CATACOMBS) { event ->
            if (!recording) return@registerIn

            val healdItem = KnitPlayer.player?.mainHandItem?.hoverName?.stripped ?: ""
            val sound = event.sound

            if (sound.location == SoundEvents.ENDER_DRAGON_HURT.location) {
                val pos = BlockPos((sound.x - 0.5).toInt(), (sound.y - 1).toInt(), (sound.z - 0.5).toInt())
                addWaypoint(WaypointType.ETHERWARP, pos)
            }

            if (sound.location == SoundEvents.GENERIC_EXPLODE.value().location) {
                if (setOf("boom TNT", "Explosive Bow").none { healdItem.contains(it) }) return@registerIn
                val pos = BlockPos((sound.x - 0.5).toInt(), (sound.y - 0.5).toInt(), (sound.z - 0.5).toInt())
                addWaypoint(WaypointType.SUPERBOOM, pos)
            }

            if (sound.location.toString().contains("break")) {
                if(!healdItem.contains("Dungeon Breaker")) return@registerIn
                val pos = BlockPos((sound.x - 0.5).toInt(), (sound.y - 0.5).toInt(), (sound.z - 0.5).toInt())
                addWaypoint(WaypointType.MINE, pos)
            }
        }


        EventBus.registerIn<TickEvent.Client>(SkyBlockIsland.THE_CATACOMBS) {
            if (!recording) return@registerIn

            val room = DungeonAPI.currentRoom ?: return@registerIn
            val loc = KnitPlayer.player?.onPos ?: return@registerIn
            val pos = room.getRelativeCoord(BlockPos(loc.x, loc.y + 1, loc.z))

            if (lastPlayerPos == null || calcDistanceSq(pos, lastPlayerPos!!) > 4) {
                currentStep.line += pos
                lastPlayerPos = pos
            }
        }

        EventBus.registerIn<RenderEvent.World.Last>(SkyBlockIsland.THE_CATACOMBS) { event ->
            if (!recording) return@registerIn

            RoutePlayer.renderRecordingRoute(currentStep, lastStep, event.context)
        }
    }

    fun nextStep() {
        stepIndex++
        if (stepIndex >= route.size) {
            route.add(StepData(mutableListOf(), mutableListOf()))
        }
    }

    fun previousStep() {
        if (stepIndex > 0) stepIndex--
    }

    fun getRoute(): List<StepData> = route

    fun startRecording() {
        val roomName = DungeonAPI.currentRoom?.name
        if (roomName == null) {
            KnitChat.modMessage("§cNot in a valid dungeon room")
            return
        }

        recording = true
        currentRoomName = roomName
        route.clear()
        stepIndex = 0
        route.add(StepData(mutableListOf(), mutableListOf()))

        KnitChat.modMessage("§aStarted route recording for $roomName")
    }

    fun addWaypoint(type: WaypointType, pos: BlockPos) {
        val room = DungeonAPI.currentRoom ?: return
        val relPos = room.getRelativeCoord(pos)

        val waypoint = WaypointData(relPos, type)
        currentStep.waypoints += waypoint

        KnitChat.modMessage("Current waypoints")

        currentStep.waypoints.forEach {
            KnitChat.modMessage("${it.type}, ${it.pos}")
        }

        if (type == WaypointType.SECRET) nextStep()
    }

    fun stopRecording() {
        recording = false
    }

    private fun Room.getRelativeCoord(pos: BlockPos) =
        pos.subtract(Vec3i(corner?.first ?: 0, 0, corner?.third ?: 0)).rotateToNorth(rotation)

    private fun BlockPos.rotateToNorth(rotation: RoomRotations): BlockPos =
        when (rotation) {
            RoomRotations.NORTH -> BlockPos(-this.x, this.y, -this.z)
            RoomRotations.WEST -> BlockPos(this.z, this.y, -this.x)
            RoomRotations.SOUTH -> BlockPos(this.x, this.y, this.z)
            RoomRotations.EAST -> BlockPos(-this.z, this.y, this.x)
            else -> this
        }
}