package xyz.meowing.krypt.features.waypoints.utils

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShapeRenderer
import net.minecraft.core.BlockPos
import net.minecraft.world.level.EmptyBlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import xyz.meowing.knit.api.render.world.RenderContext
import xyz.meowing.krypt.Krypt
import xyz.meowing.krypt.api.dungeons.DungeonAPI
import xyz.meowing.krypt.api.dungeons.enums.map.Room
import xyz.meowing.krypt.api.dungeons.enums.map.RoomRotations
import xyz.meowing.krypt.utils.WorldUtils
import xyz.meowing.krypt.utils.rendering.Render3D
import java.awt.Color

object RoutePlayer {
    fun renderRecordingRoute(data: StepData, context: RenderContext) {
        val room = DungeonAPI.currentRoom ?: return

        renderLine(data, context, room)
        renderWaypoints(data, context, room)
    }

    fun renderLine(data: StepData, context: RenderContext, room: Room) {
        if (data.line.size <= 1) return
        val startPoint = room.getRealCoord(data.line.first())
        val startPos = Vec3(startPoint.center.x, startPoint.center.y + 1, startPoint.center.z)

        Render3D.drawString("Start!", startPos, context.matrixStack())

        data.line.zipWithNext { a, b ->
            val p1 = room.getRealCoord(a)
            val p2 = room.getRealCoord(b)

            Render3D.drawLine(
                p1.center,
                p2.center,
                3f,
                Color.RED,
                context.consumers(),
                context.matrixStack()
            )
        }
    }

    private fun renderWaypoints(data: StepData, context: RenderContext, room: Room) {
        val firstMine = data.waypoints.firstOrNull { it.type == WaypointType.MINE }

        data.waypoints.forEach { waypoint ->
            val name = waypoint.type != WaypointType.MINE || waypoint == firstMine
            renderWaypoint(waypoint, context, room, name)
        }
    }

    private fun renderLastSecret(data: StepData, context: RenderContext, room: Room) {
        val secret = data.waypoints.firstOrNull { it.type == WaypointType.SECRET } ?: return
        renderWaypoint(secret, context, room)
    }

    private fun renderWaypoint(waypoint: WaypointData, context: RenderContext, room: Room, name: Boolean = true){
        val realPos = room.getRealCoord(waypoint.pos)
        val block = WorldUtils.getBlockStateAt(realPos.x, realPos.y, realPos.z)
        val stack = context.matrixStack() ?: return
        val consumers = context.consumers()

        if(waypoint.type == WaypointType.SUPERBOOM) Krypt.LOGGER.info("Rendering Superboom at, $realPos")

        if (block != null && block != Blocks.AIR) {
            if(waypoint.type == WaypointType.SUPERBOOM) Krypt.LOGGER.info("Found Block!")

            val blockShape = block.getShape(
                EmptyBlockGetter.INSTANCE,
                realPos,
                CollisionContext.of(context.camera().entity)
            )

            if (blockShape.isEmpty) return
            val camPos = context.camera().position

            ShapeRenderer.renderShape(
                stack,
                consumers.getBuffer(RenderType.lines()),
                blockShape,
                realPos.x - camPos.x,
                realPos.y - camPos.y,
                realPos.z - camPos.z,
                waypoint.type.color.rgb
            )
        } else{
            if(waypoint.type == WaypointType.SUPERBOOM) Krypt.LOGGER.info("Didnt Find block!")

            val aabb = AABB(0.0,0.0,0.0,1.0,1.0,1.0).move(realPos)

            Render3D.drawOutlinedBB(
                aabb,
                waypoint.type.color,
                consumers,
                stack
            )
        }

        val aabb = AABB(0.0,0.0,0.0,1.0,1.0,1.0).move(realPos)

        Render3D.drawOutlinedBB(
            aabb,
            waypoint.type.color,
            consumers,
            stack,
            true
        )

        if (name) Render3D.drawString(waypoint.type.label, realPos.center, stack, depth = false)
    }

    private fun Room.getRealCoord(pos: BlockPos): BlockPos = pos.rotateAroundNorth(rotation).offset(corner?.first ?: 0, 0, corner?.third ?: 0)

    private fun BlockPos.rotateAroundNorth(rotation: RoomRotations): BlockPos =
        when (rotation) {
            RoomRotations.NORTH -> BlockPos(-this.x, this.y, -this.z)
            RoomRotations.WEST -> BlockPos(-this.z, this.y, this.x)
            RoomRotations.SOUTH -> BlockPos(this.x, this.y, this.z)
            RoomRotations.EAST -> BlockPos(this.z, this.y, -this.x)
            else -> this
        }
}