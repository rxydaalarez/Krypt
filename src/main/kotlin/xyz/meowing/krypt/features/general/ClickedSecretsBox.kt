package xyz.meowing.krypt.features.general

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB
import xyz.meowing.krypt.annotations.Module
import xyz.meowing.krypt.api.dungeons.enums.map.Room
import xyz.meowing.krypt.api.location.SkyBlockIsland
import xyz.meowing.krypt.config.ConfigDelegate
import xyz.meowing.krypt.config.ui.elements.base.ElementType
import xyz.meowing.krypt.events.core.DungeonEvent
import xyz.meowing.krypt.events.core.RenderEvent
import xyz.meowing.krypt.features.Feature
import xyz.meowing.krypt.managers.config.ConfigElement
import xyz.meowing.krypt.managers.config.ConfigManager
import xyz.meowing.krypt.utils.rendering.Render3D
import java.awt.Color

@Module
object ClickedSecretsBox : Feature(
    "secretBox",
    island = SkyBlockIsland.THE_CATACOMBS
) {
    override fun addConfig() {
        ConfigManager
            .addFeature(
                "Clicked secret boxes",
                "Draw a box on clicked secrets",
                "General",
                ConfigElement(
                    "secretBox",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Box color",
                ConfigElement(
                    "secretBox.color",
                    ElementType.ColorPicker(Color.red)
                )
            )
    }

    private val color by ConfigDelegate<Color>("secretBox.color")
    private var clickedSecrets: MutableList<BlockPos> = mutableListOf()
    private val roomMap: MutableMap<Room, MutableList<BlockPos>> = mutableMapOf()

    override fun initialize() {
        register<DungeonEvent.Room.Change> { event ->
            val new = event.new
            val old = event.old
            roomMap[old] = clickedSecrets

            clickedSecrets = roomMap[new] ?: mutableListOf()
        }

        register<RenderEvent.World.Last> { event ->
            for (entry in clickedSecrets) {
                Render3D.drawOutlinedBB(
                    AABB(entry),
                    color,
                    event.context.consumers(),
                    event.context.matrixStack()
                )
            }
        }

        register<DungeonEvent.Secrets.Chest> { event ->
            clickedSecrets.add(event.blockPos)
        }

        register<DungeonEvent.Secrets.Essence> { event ->
            clickedSecrets.add(event.blockPos)
        }

        register<DungeonEvent.Secrets.Misc> { event ->
            if (event.secretType == DungeonEvent.Secrets.Type.LEVER) {
                clickedSecrets.add(event.blockPos)
            }
        }

        register<DungeonEvent.End> {
            clickedSecrets.clear()
            roomMap.clear()
        }
    }
}