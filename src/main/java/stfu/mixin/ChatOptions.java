package stfu.mixin;

import net.minecraft.client.gui.screen.option.ChatOptionsScreen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Options;

@Mixin(SimpleOptionsScreen.class)
public abstract class ChatOptions {
    @Shadow protected OptionListWidget buttonList;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/OptionListWidget;addAll([Lnet/minecraft/client/option/SimpleOption;)V", shift = At.Shift.AFTER))
    private void addMaxChatHistoryOption(CallbackInfo ci) {
        if((Object) this instanceof ChatOptionsScreen) buttonList.addAll(new SimpleOption[]{Options.maxChatHistory, Options.adminChat, Options.announceAdvancements, Options.compactChat});
    }
}
