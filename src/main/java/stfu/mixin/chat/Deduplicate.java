package stfu.mixin.chat;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.GuiMessage;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import stfu.config.Config;

import java.util.List;

@Mixin(ChatComponent.class)
public abstract class Deduplicate {
    @Unique
    private static final Style OCCURRENCES = Style.EMPTY.withColor(ChatFormatting.GRAY);
    @Shadow
    @Final
    private List<GuiMessage> allMessages;

    @Shadow(aliases = {"refreshTrimmedMessage"})
    protected abstract void refreshTrimmedMessages();

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Component compact(Component message) {
        if (Config.get().compactChat == Config.CompactChat.NEVER || allMessages.isEmpty()) return message;
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
        for (GuiMessage other : Config.get().compactChat == Config.CompactChat.ONLY_CONSECUTIVE ? List.of(allMessages.get(0)) : allMessages) {
            Component content = other.content();
            if (!content.getContents().equals(message.getContents()) || !content.getStyle().equals(message.getStyle())) continue;

            // Check siblings without occurrences count
            List<Component> siblings = content.getSiblings();
            String o = null;
            if (!siblings.isEmpty()) {
                Component last = siblings.get(siblings.size() - 1);
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
            allMessages.remove(other);
            refreshTrimmedMessages();
            break; // Trust the previous message
        }
        // Append occurrences count
        if (matches > 1) {
            if (message instanceof MutableComponent mutable) try {
                return mutable.append(Component.literal(" (" + matches + ")").setStyle(OCCURRENCES));
            } catch (UnsupportedOperationException ignored) {} // MutableText is not always mutable? in this case use copy to assure it is backed by an arraylist
            return message.copy().append(Component.literal(" (" + matches + ")").setStyle(OCCURRENCES));
        }
        return message;
    }
}
