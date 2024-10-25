package stfu.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Options;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ShutChat {
    @Unique
    private static final Style OCCURRENCES = Style.EMPTY.withColor(Formatting.GRAY);
    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    protected abstract void refresh();

    @ModifyExpressionValue(method = {"addToMessageHistory", "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"}, at =
    @At(value = "CONSTANT", args = "intValue=100"))
    private int moreHistory(int original) {
        return Options.maxChatHistory.getValue();
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void filter(Text message, CallbackInfo ci) {
        if (!(message instanceof MutableText mutable && mutable.getContent() instanceof TranslatableTextContent translatable)) return;

        if (translatable.getKey().startsWith("chat.type.advancement")) {
            if (!Options.announceAdvancements.getValue()) ci.cancel();
        } else if (translatable.getKey().equals("chat.type.admin")) {
            Options.AdminChat adminChat = Options.adminChat.getValue();
            if (adminChat == Options.AdminChat.DISABLED || (adminChat == Options.AdminChat.ONLY_PLAYERS && translatable.getArgs()[0].equals("@")))
                ci.cancel();
        }
    }

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text compact(Text message) {
        if (Options.compactChat.getValue() == Options.CompactChat.NEVER || messages.isEmpty()) return message;
        // Skip common separators
        boolean isSeparator = true;
        for (char c : message.getString().trim().toCharArray())
            if (c != ' ' && c != '=' && c != '-' && c != '_' && c != '~') {
                isSeparator = false;
                break;
            }
        if (isSeparator) return message;

        // Find matching messages
        int matches = 0;
        for (ChatHudLine other : Options.compactChat.getValue() == Options.CompactChat.ONLY_CONSECUTIVE ? List.of(messages.getFirst()) : messages) {
            Text content = other.content();
            if (!content.getContent().equals(message.getContent()) || !content.getStyle().equals(message.getStyle())) continue;

            // Check siblings without occurrences count
            List<Text> siblings = content.getSiblings();
            String o = null;
            if (!siblings.isEmpty()) {
                Text last = siblings.getLast();
                if (last.getStyle() == OCCURRENCES) {
                    String raw = last.getString();
                    if (raw != null && raw.startsWith(" (") && raw.endsWith(")")) {
                        o = raw.substring(2, raw.length() - 1);
                        siblings.removeLast();
                    }
                }
            }
            if (!siblings.equals(message.getSiblings())) continue;

            // Increment occurrences count
            if (o == null) matches = 2;
            else try {
                matches = Integer.parseInt(o) + 1;
            } catch (NumberFormatException e) {
                continue;
            }
            // remove previous message
            messages.remove(other);
            refresh();
            break; // Trust the previous message
        }
        // Append occurrences count
        if (matches > 1) try {
            ((MutableText) message).append(Text.literal(" (" + matches + ")").setStyle(OCCURRENCES));
        } catch (UnsupportedOperationException e) {// MutableText is not always mutable? in this case use copy to assure it is backed by an arraylist
            return message.copy().append(Text.literal(" (" + matches + ")").setStyle(OCCURRENCES));
        }
        return message;
    }
}
