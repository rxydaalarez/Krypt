package xyz.meowing.krypt.features.waypoints

import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.world.item.component.CustomData
import org.lwjgl.glfw.GLFW
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.api.datatype.getData
import xyz.meowing.knit.api.KnitClient
import xyz.meowing.knit.api.KnitClient.client
import xyz.meowing.krypt.annotations.Module
import xyz.meowing.krypt.api.dungeons.DungeonAPI
import xyz.meowing.krypt.api.location.SkyBlockIsland
import xyz.meowing.krypt.config.ConfigDelegate
import xyz.meowing.krypt.config.ui.elements.base.ElementType
import xyz.meowing.krypt.events.core.DungeonEvent
import xyz.meowing.krypt.events.core.LocationEvent
import xyz.meowing.krypt.events.core.MouseEvent
import xyz.meowing.krypt.events.core.PacketEvent
import xyz.meowing.krypt.events.core.RenderEvent
import xyz.meowing.krypt.features.Feature
import xyz.meowing.krypt.features.waypoints.utils.RoomWaypointHandler
import xyz.meowing.krypt.features.waypoints.utils.RouteRecorder
import xyz.meowing.krypt.managers.config.ConfigElement
import xyz.meowing.krypt.managers.config.ConfigManager
import java.awt.Color

@Module
object DungeonWaypoints : Feature(
    "dungeonWaypoints",
    island = SkyBlockIsland.THE_CATACOMBS
) {
    val overrideColors by ConfigDelegate<Boolean>("dungeonWaypoints.overrideColors")
    val overrideOnSave by ConfigDelegate<Boolean>("dungeonWaypoints.overrideOnSave")

    val onlyRenderAfterClear by ConfigDelegate<Boolean>("dungeonWaypoints.onlyRenderAfterClear")
    val stopRenderAfterGreen by ConfigDelegate<Boolean>("dungeonWaypoints.stopRenderAfterGreen")
    val renderText by ConfigDelegate<Boolean>("dungeonWaypoints.renderText")
    val textRenderDistance by ConfigDelegate<Double>("dungeonWaypoints.textRenderDistance")
    val textScale by ConfigDelegate<Double>("dungeonWaypoints.textScale")

    val startColor by ConfigDelegate<Color>("dungeonWaypoints.startColor")
    val mineColor by ConfigDelegate<Color>("dungeonWaypoints.mineColor")
    val superBoomColor by ConfigDelegate<Color>("dungeonWaypoints.superboomColor")
    val etherWarpColor by ConfigDelegate<Color>("dungeonWaypoints.etherwarpColor")
    val secretColor by ConfigDelegate<Color>("dungeonWaypoints.secretColor")
    val batColor by ConfigDelegate<Color>("dungeonWaypoints.batColor")
    val leverColor by ConfigDelegate<Color>("dungeonWaypoints.leverColor")

    override fun addConfig() {
        ConfigManager
            .addFeature(
                "Dungeon waypoints",
                "Shows waypoints for secrets in dungeon rooms",
                "General",
                ConfigElement(
                    "dungeonWaypoints",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Only render after clear",
                ConfigElement(
                    "dungeonWaypoints.onlyRenderAfterClear",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Stop render after green",
                ConfigElement(
                    "dungeonWaypoints.stopRenderAfterGreen",
                    ElementType.Switch(true)
                )
            )
            .addFeatureOption(
                "Render text",
                ConfigElement(
                    "dungeonWaypoints.renderText",
                    ElementType.Switch(true)
                )
            )
            .addFeatureOption(
                "Text scale",
                ConfigElement(
                    "dungeonWaypoints.textScale",
                    ElementType.Slider(0.1, 4.0, 1.0, false)
                )
            )
            .addFeatureOption(
                "Text render distance",
                ConfigElement(
                    "dungeonWaypoints.textRenderDistance",
                    ElementType.Slider(1.0, 20.0, 10.0, false)
                )
            )
            .addFeatureOption(
                "Override colors",
                ConfigElement(
                    "dungeonWaypoints.overrideColors",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Override on save",
                ConfigElement(
                    "dungeonWaypoints.overrideOnSave",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Start color",
                ConfigElement(
                    "dungeonWaypoints.startColor",
                    ElementType.ColorPicker(Color(0, 255, 0, 255))
                )
            )
            .addFeatureOption(
                "Mine color",
                ConfigElement(
                    "dungeonWaypoints.mineColor",
                    ElementType.ColorPicker(Color(139, 69, 19, 255))
                )
            )
            .addFeatureOption(
                "Superboom color",
                ConfigElement(
                    "dungeonWaypoints.superboomColor",
                    ElementType.ColorPicker(Color(255, 0, 0, 255))
                )
            )
            .addFeatureOption(
                "Etherwarp color",
                ConfigElement(
                    "dungeonWaypoints.etherwarpColor",
                    ElementType.ColorPicker(Color(147, 51, 234, 255))
                )
            )
            .addFeatureOption(
                "Secret color",
                ConfigElement(
                    "dungeonWaypoints.secretColor",
                    ElementType.ColorPicker(Color(255, 215, 0, 255))
                )
            )
            .addFeatureOption(
                "Bat color",
                ConfigElement(
                    "dungeonWaypoints.batColor",
                    ElementType.ColorPicker(Color(255, 105, 180, 255))
                )
            )
            .addFeatureOption(
                "Lever color",
                ConfigElement(
                    "dungeonWaypoints.leverColor",
                    ElementType.ColorPicker(Color(0, 191, 255, 255))
                )
            )
            .addFeatureOption(
                "Start Recording",
                ConfigElement(
                    "dungeonWaypoints.startRecording",
                    ElementType.Button("Start Recording") { RouteRecorder.startRecording() }
                )
            )
            .addFeatureOption(
                "Stop Recording",
                ConfigElement(
                    "dungeonWaypoints.stopRecording",
                    ElementType.Button("Stop Recording") { RouteRecorder.stopRecording() }
                )
            )
            .addFeatureOption(
                "Reload Routes (Local)",
                ConfigElement(
                    "dungeonWaypoints.reloadLocal",
                    ElementType.Button("Reload from Local") {
                        //WaypointRegistry.reloadFromLocal(notifyUser = true)
                    }
                )
            )
    }

    override fun initialize() {

    }
}