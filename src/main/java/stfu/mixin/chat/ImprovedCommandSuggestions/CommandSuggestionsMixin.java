package stfu.mixin.chat.ImprovedCommandSuggestions;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin {
    @Shadow
    @Final
    EditBox input;

    @Shadow
    private static int getLastWordIndex(String input) {
        return 0;
    }

    @Inject(method = "sortSuggestions", at = @At("HEAD"), cancellable = true)
    private void sortSuggestions(Suggestions suggestions, CallbackInfoReturnable<List<Suggestion>> cir) {
        String string = this.input.getValue().substring(0, this.input.getCursorPosition());
        if(string.startsWith("/")) string = string.substring(1);
        string = string.substring(getLastWordIndex(string)).toLowerCase(Locale.ROOT);
        if(string.startsWith("#")) string = string.substring(1);
        if(string.contains(":")) string = string.substring(string.indexOf(':') + 1);

        List<Suggestion> list = new ArrayList<>();
        List<Suggestion> list2 = new ArrayList<>();
        List<Suggestion> list3 = new ArrayList<>();

        for (Suggestion suggestion : suggestions.getList()) {
            String text = suggestion.getText();
            if (text.startsWith(string) || (text.indexOf(':') > 0 && text.split(":", 2)[1].startsWith(string))) list.add(suggestion);
            else if(text.contains(string)) list2.add(suggestion);
            else list3.add(suggestion);
        }

        list.addAll(list2);
        list.addAll(list3);
        cir.setReturnValue(list);
    }
}
