package stfu.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

import static stfu.Main.client;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsMixin {
    @ModifyReturnValue(method = "getOptions", at = @At("RETURN"))
    private static SimpleOption<?>[] removeNarratorHotkeySetting(SimpleOption<?>[] original) {
        return Arrays.stream(original).filter(option -> !option.equals(client.options.getNarratorHotkey())).toArray(SimpleOption[]::new);
    }
}
