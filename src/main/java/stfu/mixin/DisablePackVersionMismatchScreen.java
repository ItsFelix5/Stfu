package stfu.mixin;

import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TransferableSelectionList.PackEntry.class)
public class DisablePackVersionMismatchScreen {
    @Redirect(method = "handlePackSelection", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackCompatibility;isCompatible()Z"))
    private boolean isCompatible(PackCompatibility instance) {
        return true;
    }
}
