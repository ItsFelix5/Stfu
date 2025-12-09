package stfu.mixin.RemoveOverlay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//? >1.20.1 {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
*///?} else
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {
    @Shadow @Final private boolean fadeIn;

    @ModifyConstant(method = "render", constant = @Constant(floatValue = 2.0F))
    private float disableFade(float progress) {
        return Config.get().disableFade? 1F : progress;
    }

    @Inject(method = "isPauseScreen", at = @At("HEAD"), cancellable = true)
    private void pausesGame(CallbackInfoReturnable<Boolean> cir) {
        if (Config.get().disableSplash && fadeIn) cir.setReturnValue(false);
    }

    //? >1.21.8 {
    /*@Inject(method = "isReadyToFadeOut", at = @At("HEAD"), cancellable = true)
    private void isInGracePeriod(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
    *///?}
    //? >1.20.1 {
    /*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
    private void render(GuiGraphics instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!Config.get().disableSplash || !fadeIn) original.call(instance, x1, y1, x2, y2, color);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIIIII)V"))
    private void render(GuiGraphics instance, RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (!Config.get().disableSplash || !fadeIn) original.call(instance, pipeline, sprite, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color);
    }
    *///?} else {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"))
    private void render(GuiGraphics instance, RenderType renderType, int i, int j, int k, int l, int m, Operation<Void> original) {
        if (!Config.get().disableSplash || !fadeIn) original.call(instance, renderType, i, j, k, l, m);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V"))
    private void render(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, int k, int l, float f, float g, int m, int n, int o, int p, Operation<Void> original) {
        if (!Config.get().disableSplash || !fadeIn) original.call(instance, resourceLocation, i, j, k, l, f, g, m, n, o, p);
    }
    //?}
}
