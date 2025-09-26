package stfu.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.config.Config;

@Mixin(GameRenderer.class)
public class NoNightVisionFlicker {
    @Redirect(method = "getNightVisionStrength", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isDurationBelow(I)Z"))
    private static boolean getNightVisionStrength(StatusEffectInstance instance, int duration) {
        return Config.get().nightVisionFlicker && instance.isDurationBelow(duration);
    }
}
