package stfu.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

@Mixin(GameRenderer.class)
public class NoNightVisionFlicker {
    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void getNightVisionScale(LivingEntity entity, float tickProgress, CallbackInfoReturnable<Float> cir) {
        if (Config.get().nightVisionFlicker) return;
        float statusEffectDuration = entity.getEffect(MobEffects.NIGHT_VISION).getDuration();
        cir.setReturnValue(statusEffectDuration == -1 ? 1F : Math.min((statusEffectDuration - tickProgress) / 20F, 1F));
    }
}
