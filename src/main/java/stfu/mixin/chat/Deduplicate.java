package stfu.mixin.chat;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import stfu.config.Config;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class Deduplicate {
    @Unique
    private static final Style OCCURRENCES = Style.EMPTY.withColor(Formatting.GRAY);
    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    protected abstract void refresh();

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Text compact(Text message) {
        if (Config.get().compactChat == Config.CompactChat.NEVER || messages.isEmpty()) return message;
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
        for (ChatHudLine other : Config.get().compactChat == Config.CompactChat.ONLY_CONSECUTIVE ? List.of(messages.get(0)) : messages) {
            Text content = other.content();
            if (!content.getContent().equals(message.getContent()) || !content.getStyle().equals(message.getStyle())) continue;

            // Check siblings without occurrences count
            List<Text> siblings = content.getSiblings();
            String o = null;
            if (!siblings.isEmpty()) {
                Text last = siblings.get(siblings.size() - 1);
                if (last.getStyle() == OCCURRENCES) {
                    String raw = last.getString();
                    if (raw != null && raw.startsWith(" (") && raw.endsWith(")")) {
                        o = raw.substring(2, raw.length() - 1);
                        siblings.remove(siblings.size() - 1);
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
        if (matches > 1) {
            if (message instanceof MutableText mutable) try {
                return mutable.append(Text.literal(" (" + matches + ")").setStyle(OCCURRENCES));
            } catch (UnsupportedOperationException ignored) {} // MutableText is not always mutable? in this case use copy to assure it is backed by an arraylist
            return message.copy().append(Text.literal(" (" + matches + ")").setStyle(OCCURRENCES));
        }
        return message;
    }
}
