//? <= 1.21.8 {
package stfu.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class AllowScreensInPortals {
    @Redirect(method = {"handlePortalTransitionEffect", "handleNetherPortalClient"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;isPauseScreen()Z"))
    public boolean tickNausea(Screen instance) {
        return true;
    }
}
//?}