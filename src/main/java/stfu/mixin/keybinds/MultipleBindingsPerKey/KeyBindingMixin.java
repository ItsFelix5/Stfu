//? <= 1.21.8 {
package stfu.mixin.keybinds.MultipleBindingsPerKey;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
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

    @Redirect(method = "resetMapping", at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V"))
    private static void clearMap(Map<?, ?> instance) {
        KeybindHolder.KEY_TO_BINDINGS.clear();
    }

    @Redirect(method = "resetMapping", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static <K, V> V addBinding(Map<K, V> instance, K k, V v) {
        KeybindHolder.KEY_TO_BINDINGS.computeIfAbsent((InputConstants.Key) k, unused -> new HashSet<>()).add((KeyMapping) v);
        return v;
    }

    @Redirect(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at = @At(value = "INVOKE", target =
            "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1))
    private Object add(Map<?, ?> instance, Object key, Object value) {
        return KeybindHolder.KEY_TO_BINDINGS.computeIfAbsent((InputConstants.Key) key, k -> new HashSet<>()).add((KeyMapping) value);
    }
}
//?}