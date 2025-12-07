package xyz.meowing.krypt.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.meowing.krypt.events.EventBus;
import xyz.meowing.krypt.events.core.EntityEvent;

/**
 * Contains modified code from Fabric-API.
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;interactAt(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
            ),
            method = "startUseItem",
            cancellable = true
    )
    private void krypt$entityInteract(CallbackInfo ci, @Local Entity entity) {
        if (EventBus.INSTANCE.post(new EntityEvent.Interact(entity))) ci.cancel();
    }
}
