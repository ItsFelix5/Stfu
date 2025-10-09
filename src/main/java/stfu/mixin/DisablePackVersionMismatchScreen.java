package stfu.mixin;

import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.resource.ResourcePackCompatibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PackListWidget.ResourcePackEntry.class)
public class DisablePackVersionMismatchScreen {
    @Redirect(method = "enable", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackCompatibility;isCompatible()Z"))
    private boolean isCompatible(ResourcePackCompatibility instance) {
        return true;
    }
}
