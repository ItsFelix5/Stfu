package stfu.mixin.chat;

//? >1.21.11 {
/*import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
*///? } else {
 import net.minecraft.client.GuiMessage;
 import net.minecraft.client.GuiMessageTag;
//? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiMessage.class)
public class RemoveUnsignedIcon {
    @Inject(method = "tag", at = @At("HEAD"), cancellable = true)
    private void indicator(CallbackInfoReturnable<GuiMessageTag> cir) {
        cir.setReturnValue(null);
    }
}
