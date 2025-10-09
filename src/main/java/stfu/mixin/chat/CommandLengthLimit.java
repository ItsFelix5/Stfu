package stfu.mixin.chat;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class CommandLengthLimit {
    @Shadow
    protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void init(CallbackInfo ci) {
        chatField.setMaxLength(Integer.MAX_VALUE);
    }

    @Inject(method = "onChatFieldUpdate", at = @At(value = "HEAD"))
    private void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        if (chatText.startsWith("/") || chatText.isEmpty()) chatField.setMaxLength(Integer.MAX_VALUE);
        else {
            //? if > 1.21 {
            if(chatField.getCursor() > 256) chatField.setCursor(256, false);
            chatField.setMaxLength(256);
            //?} else {
            /*if(chatField.getCursor() > 256) chatField.setCursor(256);
            chatField.setMaxLength(256);
            chatField.setSelectionEnd(chatField.getCursor());
            *///?}
        }
    }

    @Inject(method = "normalize", at = @At(value = "HEAD"), cancellable = true)
    private void normalize(String chatText, CallbackInfoReturnable<String> cir) {
        if (chatText.startsWith("/"))
            cir.setReturnValue(StringUtils.normalizeSpace(chatText.trim()));
    }
}
