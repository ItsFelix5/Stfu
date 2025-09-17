package stfu.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Shadow
    protected TextFieldWidget chatField;
    @Unique
    private boolean isCommand = true;

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void init(CallbackInfo ci) {
        chatField.setMaxLength(Integer.MAX_VALUE);
    }

    @Inject(method = "onChatFieldUpdate", at = @At(value = "HEAD"))
    private void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        if (chatText.startsWith("/") || chatText.isEmpty()) {
            if (!isCommand) {
                isCommand = true;
                chatField.setMaxLength(Integer.MAX_VALUE);
            }
        } else if (isCommand) {
            isCommand = false;
            chatField.setMaxLength(256);
        }
    }

    @Inject(method = "normalize", at = @At(value = "HEAD"), cancellable = true)
    private void normalize(String chatText, CallbackInfoReturnable<String> cir) {
        if (chatText.startsWith("/"))
            cir.setReturnValue(StringUtils.normalizeSpace(chatText.trim()));
    }
}
