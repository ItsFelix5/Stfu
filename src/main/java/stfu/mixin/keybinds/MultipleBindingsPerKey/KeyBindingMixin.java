//? <= 1.21.8 {
/*package stfu.mixin.keybinds.MultipleBindingsPerKey;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.KeybindHolder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(KeyMapping.class)
public class KeyBindingMixin {
    @Shadow
    public InputConstants.Key key;

    @Shadow
    @Final
    private static Map<String, KeyMapping> ALL;

    @Inject(method = "click", at = @At("HEAD"), order = 1001, cancellable = true)
    private static void onKeyPressed(InputConstants.Key key, CallbackInfo ci) {
        ci.cancel();
        KeybindHolder.KEY_TO_BINDINGS.getOrDefault(key, Set.of()).forEach(keyBinding -> keyBinding.clickCount++);
    }

    @Inject(method = "set", at = @At("HEAD"), order = 1001, cancellable = true)
    private static void setKeyPressed(InputConstants.Key key, boolean pressed, CallbackInfo ci) {
        ci.cancel();
        KeybindHolder.KEY_TO_BINDINGS.getOrDefault(key, Set.of()).forEach(keyBinding -> keyBinding.setDown(pressed));
    }

    @Inject(method = "resetMapping", at = @At("HEAD"))
    private static void clearMap(CallbackInfo ci) {
        KeybindHolder.KEY_TO_BINDINGS.clear();
        for (KeyMapping keyMapping : ALL.values()) {
            KeybindHolder.KEY_TO_BINDINGS.computeIfAbsent(keyMapping.key, unused -> new HashSet<>()).add(keyMapping);
        }
    }

    @Inject(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at = @At("TAIL"))
    private void add(String string, InputConstants.Type type, int i, String string2, CallbackInfo ci) {
        KeybindHolder.KEY_TO_BINDINGS.computeIfAbsent(key, k -> new HashSet<>()).add((KeyMapping) (Object) this);
    }
}
*///?}