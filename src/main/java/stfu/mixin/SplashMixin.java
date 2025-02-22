package stfu.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

import static stfu.Main.client;

@Mixin(SplashOverlay.class)
public class SplashMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceReload;throwException()V"))
    private void removeOverlay(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(Config.conf.disableFade) client.setOverlay(null);
    }
}
