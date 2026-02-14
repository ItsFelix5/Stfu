package stfu.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;

@Mixin(WorldOpenFlows.class)
abstract class DisableWorldAdvice {
    @Inject(method = "confirmWorldCreation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", ordinal = 0), cancellable = true)
    private static void confirmWorldCreation(Minecraft client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, boolean bypassWarnings, CallbackInfo ci) {
        if (Config.get().disableWorldAdvice) {
            loader.run();
            ci.cancel();
        }
    }

    @Redirect(method = {"openWorldCheckWorldStemCompatibility", "doLoadLevel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private Lifecycle openWorldCheckWorldStemCompatibility(WorldData saveProperties) {
        if (Config.get().disableWorldAdvice) return Lifecycle.stable();
        return saveProperties.worldGenSettingsLifecycle();
    }
}
