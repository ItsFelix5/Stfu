package stfu.mixin.RemoveOverlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;" /*? if >1.21.8 {*/, ordinal = 2/*?}*/))
    private Overlay overlay(MinecraftClient instance){
        return null;
    }
}
