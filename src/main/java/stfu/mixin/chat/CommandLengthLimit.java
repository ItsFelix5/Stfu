package stfu.mixin.chat;

import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.components.EditBox;
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
    protected EditBox input;

    @Inject(method = "init", at = @At(value = "RETURN"))
    private void init(CallbackInfo ci) {
        input.setMaxLength(Integer.MAX_VALUE);
    }

    @Inject(method = "onEdited", at = @At(value = "HEAD"))
    private void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        if (chatText.startsWith("/") || chatText.isEmpty()) input.setMaxLength(Integer.MAX_VALUE);
        else {
            //? > 1.21 {
            if(input.getCursorPosition() > 256) input.moveCursorTo(256, false);
            input.setMaxLength(256);
            //?} else {
            /*if(input.getCursorPosition() > 256) input.setCursorPosition(256);
            input.setMaxLength(256);
            input.setHighlightPos(input.getCursorPosition());
            *///?}
        }
    }

    @Inject(method = "normalizeChatMessage", at = @At(value = "HEAD"), cancellable = true)
    private void normalize(String chatText, CallbackInfoReturnable<String> cir) {
        if (chatText.startsWith("/"))
            cir.setReturnValue(StringUtils.normalizeSpace(chatText.trim()));
    }
}
