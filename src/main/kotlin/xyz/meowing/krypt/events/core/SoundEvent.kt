package xyz.meowing.krypt.events.core

import net.minecraft.client.resources.sounds.SoundInstance
import xyz.meowing.knit.api.events.CancellableEvent

sealed class SoundEvent {
    class Play(
        val sound: SoundInstance
    ) : CancellableEvent()
}