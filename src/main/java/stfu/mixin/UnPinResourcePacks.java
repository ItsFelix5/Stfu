package stfu.mixin;

import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pack.class)
public class UnPinResourcePacks {
    @Inject(method = "isFixedPosition", at = @At("HEAD"), cancellable = true)
    private void isFixedPosition(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
