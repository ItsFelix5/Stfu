package stfu.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static stfu.Main.client;

@Mixin(InGameHud.class)
public abstract class HudMixin {
    @Shadow
    @Nullable
    protected abstract LivingEntity getRiddenEntity();

    @Shadow
    protected abstract int getHeartCount(@Nullable LivingEntity entity);

    @ModifyVariable(method = "renderMountHealth", at = @At(value = "STORE"), ordinal = 2)
    private int higherMountHealth(int y) {
        return client.interactionManager.hasStatusBars() ? y - 10 : y;
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"))
    private int alwaysRenderFood(InGameHud inGameHud, LivingEntity entity) {
        return 0;
    }

    @Inject(method = "getAirBubbleY", at = @At(value = "RETURN"), cancellable = true)
    private void moveAirUp(int heartCount, int top, CallbackInfoReturnable<Integer> cir) {
        LivingEntity entity = getRiddenEntity();
        if (entity != null) cir.setReturnValue(cir.getReturnValue() - getHeartCount(entity));
    }
}