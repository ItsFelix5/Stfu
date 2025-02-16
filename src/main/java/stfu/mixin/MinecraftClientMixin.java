package stfu.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ReconfiguringScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.Config;
import stfu.EmptyScreen;

@Mixin(value = MinecraftClient.class, priority = 999)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;
    @Shadow public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Redirect(method = "render(Z)V", at = @At(value = "INVOKE", target = "java/lang/Thread.yield()V"))
    private void removeYield(){
        if(!Config.get().disableYield) Thread.yield();
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
