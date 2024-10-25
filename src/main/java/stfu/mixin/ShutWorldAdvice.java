package stfu.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServerLoader.class)
abstract class ShutWorldAdvice {
    @Inject(method = "tryLoad(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;Lcom/mojang/serialization/Lifecycle;Ljava/lang/Runnable;Z)V", at =
    @At("HEAD"), cancellable = true)
    private static void justLoad(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, boolean bypassWarnings,
                                 CallbackInfo ci) {
        loader.run();
        ci.cancel();
    }

    @Inject(method = "showBackupPromptScreen", at = @At(value = "HEAD"), cancellable = true)
    private void noPrompt(Screen parent, String levelName, boolean customized, Runnable callback, CallbackInfo ci) {
        if (!customized) {
            ci.cancel();
            callback.run();
        }
    }
}
