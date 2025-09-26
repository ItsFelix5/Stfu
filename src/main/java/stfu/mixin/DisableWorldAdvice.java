package stfu.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;

@Mixin(IntegratedServerLoader.class)
abstract class DisableWorldAdvice {
    @Inject(method = "tryLoad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1), cancellable = true)
    private static void tryLoad(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, boolean bypassWarnings, CallbackInfo ci) {
        if (Config.get().disableWorldAdvice) {
            loader.run();
            ci.cancel();
        }
    }

    @Redirect(method = {"start(Lnet/minecraft/client/gui/screen/Screen;Ljava/lang/String;ZZ)V", "checkBackupAndStart"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SaveProperties;getLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private Lifecycle checkBackupAndStart(SaveProperties saveProperties) {
        if (Config.get().disableWorldAdvice) return Lifecycle.stable();
        return saveProperties.getLifecycle();
    }
}
