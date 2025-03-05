package stfu.mixin;

import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Config;

@Mixin(targets = "net.minecraft.client.font.TextRenderer$Drawer")
public class TextRendererMixin {
    @Inject(method = "getShadowColor", at = @At("HEAD"), cancellable = true)
    private void drawLayer(Style style, int textColor, CallbackInfoReturnable<Integer> cir) {
        if(!Config.get().textShadow) cir.setReturnValue(0);
    }
}
