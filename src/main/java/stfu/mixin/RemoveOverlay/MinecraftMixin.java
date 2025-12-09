package stfu.mixin.RemoveOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;" /*? >1.21.8 {*//*, ordinal = 2*//*?}*/, opcode = Opcodes.GETFIELD))
    private Overlay overlay(Minecraft instance){
        Overlay overlay = instance.getOverlay();
        return overlay != null && overlay.isPauseScreen()? overlay : null;
    }
}
