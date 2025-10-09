package stfu.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

@Mixin(GameRenderer.class)
public class NoNightVisionFlicker {
    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void getNightVisionStrength(LivingEntity entity, float tickProgress, CallbackInfoReturnable<Float> cir) {
        if (Config.get().nightVisionFlicker) return;
        float statusEffectInstance = ((float) entity.getStatusEffect(StatusEffects.NIGHT_VISION).getDuration() - tickProgress) / 20F;
        cir.setReturnValue(statusEffectInstance == -1 ? 1F : Math.min(statusEffectInstance, 1F));
    }
}
