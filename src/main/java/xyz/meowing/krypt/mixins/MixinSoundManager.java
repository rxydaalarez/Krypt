package xyz.meowing.krypt.mixins;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import xyz.meowing.knit.api.render.KnitResolution;
import xyz.meowing.krypt.events.EventBus;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.meowing.krypt.events.core.MouseEvent;
import xyz.meowing.krypt.events.core.SoundEvent;

@Mixin(SoundManager.class)
public abstract class MixinSoundManager {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void onPlaySound(SoundInstance soundInstance, CallbackInfo ci) {
        if(EventBus.INSTANCE.post(new SoundEvent.Play(soundInstance))) ci.cancel();
    }
}