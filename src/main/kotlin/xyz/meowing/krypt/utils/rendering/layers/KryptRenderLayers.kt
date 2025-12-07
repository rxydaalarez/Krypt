package xyz.meowing.krypt.utils.rendering.layers

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.CompositeState
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.util.TriState

/**
 * Modified from Devonian's RenderLayers.
 * Original File: [GitHub](https://github.com/Synnerz/devonian/blob/1ad3ce3a40d4f6409eaa5407d1b180ba293edb43/src/main/kotlin/com/github/synnerz/devonian/utils/render/DLayers.kt)
 */
object KryptRenderLayers {
    val LINE_LIST: RenderType = RenderType.create(
        "line-list", RenderType.TRANSIENT_BUFFER_SIZE,
        KryptRenderPipelines.LINE_LIST,
        CompositeState.builder()
            .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val LINE_LIST_ESP: RenderType = RenderType.create(
        "line-list-esp", RenderType.TRANSIENT_BUFFER_SIZE,
        KryptRenderPipelines.LINE_LIST_ESP,
        CompositeState.builder().createCompositeState(false)
    )

    val FILLED_THROUGH_WALLS: RenderType.CompositeRenderType = RenderType.create(
        "filled_through_walls", RenderType.TRANSIENT_BUFFER_SIZE, false, true,
        KryptRenderPipelines.FILLED_THROUGH_WALLS,
        CompositeState.builder()
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val BEACON_BEAM_OPAQUE: RenderType.CompositeRenderType = RenderType.create(
        "beacon_beam_opaque", 1536, false, true,
        KryptRenderPipelines.BEACON_BEAM_OPAQUE,
        CompositeState
            .builder()
            .setTextureState(
                RenderStateShard.TextureStateShard(
                    BeaconRenderer.BEAM_LOCATION,
                    //#if MC < 1.21.7
                    TriState.FALSE,
                    //#endif
                    false
                )
            )
            .createCompositeState(false)
    )

    val BEACON_BEAM_OPAQUE_THROUGH_WALLS: RenderType.CompositeRenderType = RenderType.create(
        "beacon_beam_opaque_through_walls", 1536, false, true,
        KryptRenderPipelines.BEACON_BEAM_OPAQUE_THROUGH_WALLS,
        CompositeState
            .builder()
            .setTextureState(
                RenderStateShard.TextureStateShard(
                    BeaconRenderer.BEAM_LOCATION,
                    //#if MC < 1.21.7
                    TriState.FALSE,
                    //#endif
                    false
                )
            )
            .createCompositeState(false)
    )

    val BEACON_BEAM_TRANSLUCENT: RenderType.CompositeRenderType = RenderType.create(
        "beacon_beam_translucent", 1536, false, true,
        KryptRenderPipelines.BEACON_BEAM_TRANSLUCENT,
        CompositeState
            .builder()
            .setTextureState(
                RenderStateShard.TextureStateShard(
                    BeaconRenderer.BEAM_LOCATION,
                    //#if MC < 1.21.7
                    TriState.FALSE,
                    //#endif
                    false
                )
            )
            .createCompositeState(false)
    )

    val BEACON_BEAM_TRANSLUCENT_THROUGH_WALLS: RenderType.CompositeRenderType = RenderType.create(
        "devonian_beacon_beam_translucent_esp",1536,false,true,
        KryptRenderPipelines.BEACON_BEAM_TRANSLUCENT_THROUGH_WALLS,
        CompositeState
            .builder()
            .setTextureState(
                RenderStateShard.TextureStateShard(
                    BeaconRenderer.BEAM_LOCATION,
                    //#if MC < 1.21.7
                    TriState.FALSE,
                    //#endif
                    false
                )
            )
            .createCompositeState(false)
    )
}