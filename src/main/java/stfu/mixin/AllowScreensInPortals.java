package stfu.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class AllowScreensInPortals {
//? <= 1.21.8 {
    /*@Redirect(method = {"handlePortalTransitionEffect", "handleNetherPortalClient", "handleConfusionTransitionEffect"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;isPauseScreen()Z"))
*///? } else
    @Redirect(method = "handlePortalTransitionEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;isAllowedInPortal()Z"))
    public boolean tickNausea(Screen instance) {
        return true;
    }
}