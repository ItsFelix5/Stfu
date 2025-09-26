package stfu.mixin.chat;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;

@Mixin(ChatHud.class)
public abstract class Filter {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void filter(Text message, CallbackInfo ci) {
        if (!(message instanceof MutableText mutable && mutable.getContent() instanceof TranslatableTextContent translatable)) return;

        if (translatable.getKey().startsWith("chat.type.advancement")) {
            if (!Config.get().announceAdvancements) ci.cancel();
        } else if (translatable.getKey().equals("chat.type.admin")) {
            Config.AdminChat adminChat = Config.get().adminChat;
            if (adminChat == Config.AdminChat.DISABLED || (adminChat == Config.AdminChat.ONLY_PLAYERS && translatable.getArgs()[0].equals("@")))
                ci.cancel();
        }
    }
}
