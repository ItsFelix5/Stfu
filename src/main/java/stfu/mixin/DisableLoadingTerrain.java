package stfu.mixin;

import net.minecraft.client.Minecraft;
//? <= 1.21.8 {
import net.minecraft.client.gui.screens.LevelLoadingScreen;
//?}
//? > 1.21.6 {
/*import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import net.minecraft.network.Connection;
import static stfu.Main.client;
*///?}
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import stfu.config.Config;

@Mixin(Minecraft.class)
public abstract class DisableLoadingTerrain {
    @Shadow @Nullable public ClientLevel level;

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Screen setScreen(Screen screen) {
        if (!Config.get().disableLoadingTerrain) return screen;
        //? <= 1.21.8 {
        if (screen instanceof LevelLoadingScreen) {
            if (level == null) return new Screen(Component.empty()) {};
            else return null;
        }
        //?}
        //? > 1.21.6 {
        /*if (screen instanceof ServerReconfigScreen) {
            final Connection connection = client.getConnection().getConnection();
            return new Screen(Component.empty()) {
                @Override
                public void tick() {
                    if (connection.isConnected()) connection.tick();
                    else connection.handleDisconnection();
                }
            };
        }
        *///?}
        return screen;
    }
}
