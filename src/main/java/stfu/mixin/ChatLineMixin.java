package stfu.mixin;

import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHudLine.class)
public class ChatLineMixin {
    @Inject(method = "getIcon", at = @At("HEAD"), cancellable = true)
    private void shutIcon(CallbackInfoReturnable<MessageIndicator> cir) {
        cir.setReturnValue(null);
    }
}
