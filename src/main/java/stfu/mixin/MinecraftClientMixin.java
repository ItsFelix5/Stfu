package stfu.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ReconfiguringScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;
import stfu.EmptyScreen;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;
    @Shadow public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(RunArgs args, CallbackInfo ci) {
        Thread.currentThread().setPriority(Runtime.getRuntime().availableProcessors() > 4?8:5); // Render Thread
    }

    @Redirect(method = "render(Z)V", at = @At(value = "INVOKE", target = "java/lang/Thread.yield()V"))
    private void removeYield(){// This seems to have a positive effect but it probably has a reason of existence
    }

    @ModifyVariable(at = @At("HEAD"), method = "setScreen", ordinal = 0, argsOnly = true)
    public Screen setScreen(Screen screen) {
        if(Config.get().disableLoadingTerrain) {
            if (screen instanceof ReconfiguringScreen) screen = new EmptyScreen.Configuration(getNetworkHandler().getConnection());
            else if (screen instanceof DownloadingTerrainScreen) {
                if(world == null) screen = new EmptyScreen();
                else screen = null;
            }
        }
        return screen;
    }
}
