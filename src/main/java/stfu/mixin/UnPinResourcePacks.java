package stfu.mixin;

import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackProfile.class)
public class UnPinResourcePacks {
    @Inject(method = "isPinned", at = @At("HEAD"), cancellable = true)
    private void isPinned(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
