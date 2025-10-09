package stfu.mixin;

import net.minecraft.client.MinecraftClient;
//? if <= 1.21.8 {
/*import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
*///?}
//? if > 1.21.6 {
import net.minecraft.client.gui.screen.ReconfiguringScreen;
import net.minecraft.network.ClientConnection;
import static stfu.Main.client;
//?}
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import stfu.config.Config;

@Mixin(MinecraftClient.class)
public abstract class DisableLoadingTerrain {
    @Shadow @Nullable public ClientWorld world;

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Screen setScreen(Screen screen) {
        if (!Config.get().disableLoadingTerrain) return screen;
        //? if <= 1.21.8 {
        /*if (screen instanceof DownloadingTerrainScreen) {
            if (world == null) return new Screen(Text.empty()) {};
            else return null;
        }
        *///?}
        //? if > 1.21.6 {
        if (screen instanceof ReconfiguringScreen) {
            final ClientConnection connection = client.getNetworkHandler().getConnection();
            return new Screen(Text.empty()) {
                @Override
                public void tick() {
                    if (connection.isOpen()) connection.tick();
                    else connection.handleDisconnection();
                }
            };
        }
        //?}
        return screen;
    }
}
