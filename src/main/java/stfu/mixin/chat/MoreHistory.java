package stfu.mixin.chat;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import stfu.config.Config;

@Mixin(ChatHud.class)
public abstract class MoreHistory {
    @ModifyConstant(method = {"addMessage(Lnet/minecraft/text/Text;)V", "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", "addVisibleMessage"},
            constant = @Constant(intValue = 100))
    private int moreHistory(int original) {
        return Config.get().maxChatHistory;
    }
}
