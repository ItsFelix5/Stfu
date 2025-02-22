package stfu.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.Config;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(method = "updateFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier(ZF)F"))
    private float updateFovMultiplier(AbstractClientPlayerEntity instance, boolean firstPerson, float fovEffectScale) {
        if (fovEffectScale == 0.0F) {
            if(firstPerson && instance.isUsingSpyglass()) return 0.01F;
            else return 1F;
        }
        return instance.getFovMultiplier(firstPerson, fovEffectScale);
    }

    @Redirect(method = "getNightVisionStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isDurationBelow(I)Z"))
    private static boolean getNightVisionStrength(StatusEffectInstance instance, int duration) {
        return Config.conf.nightVisionFlicker && instance.isDurationBelow(duration);
    }
}
