package stfu.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Config;

import static stfu.Main.client;

@Mixin(SplashOverlay.class)
public abstract class SplashMixin {
    @Shadow @Final private boolean reloading;
    @Shadow private float progress;
    @Shadow @Final private ResourceReload reload;
    @Shadow protected abstract void renderProgressBar(DrawContext context, int minX, int minY, int maxX, int maxY, float opacity);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceReload;throwException()V"))
    private void removeOverlay(CallbackInfo ci) {
        if (Config.get().disableFade || Config.get().disableSplash) client.setOverlay(null);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (!Config.get().disableSplash || !reloading) return;
        ci.cancel();
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        if (client.currentScreen != null) client.currentScreen.renderWithTooltip(context, 0, 0, deltaTicks);
        else client.inGameHud.renderDeferredSubtitles();

        int o = (int)(Math.min(width * 0.75, height) * 0.5);
        int q = (int)(height * 0.8325);
        this.progress = this.reload.getProgress();
        this.renderProgressBar(context, width / 2 - o, q - 5, width / 2 + o, q + 5, 1.0F);
    }


    @Inject(method = "pausesGame", at = @At("HEAD"), cancellable = true)
    private void pausesGame(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
