package stfu.mixin.RemoveOverlay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//? if >1.20.1 {
import com.mojang.blaze3d.pipeline.RenderPipeline;
//?} else
/*import net.minecraft.client.render.RenderLayer;*/
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {
    @Shadow @Final private boolean reloading;

    @ModifyConstant(method = "render", constant = @Constant(floatValue = 2.0F))
    private float disableFade(float progress) {
        return Config.get().disableFade? 1F : progress;
    }

    @Inject(method = "pausesGame", at = @At("HEAD"), cancellable = true)
    private void pausesGame(CallbackInfoReturnable<Boolean> cir) {
        if (Config.get().disableSplash && reloading) cir.setReturnValue(false);
    }

    //? if >1.21.8 {
    @Inject(method = "isInGracePeriod", at = @At("HEAD"), cancellable = true)
    private void isInGracePeriod(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
    //?}
    //? if >1.20.1 {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void render(DrawContext instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!Config.get().disableSplash || !reloading) original.call(instance, x1, y1, x2, y2, color);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIFFIIIIIII)V"))
    private void render(DrawContext instance, RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (!Config.get().disableSplash || !reloading) original.call(instance, pipeline, sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color);
    }
    //?} else {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    private void render(DrawContext instance, RenderLayer layer, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!Config.get().disableSplash || !reloading) original.call(instance, layer, x1, y1, x2, y2, color);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIFFIIII)V"))
    private void render(DrawContext instance, Identifier texture, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, Operation<Void> original) {
        if (!Config.get().disableSplash || !reloading) original.call(instance, texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
    }
    *///?}
}
