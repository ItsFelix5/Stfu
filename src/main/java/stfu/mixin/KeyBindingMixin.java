package stfu.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.KeybindHolder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    /**
     * @author ItsFelix5
     * @reason To allow multiple keybindings to be bound to the same key
     */
    @Overwrite
    public static void onKeyPressed(InputUtil.Key key) {
        KeybindHolder.KEY_TO_BINDINGS.getOrDefault(key, Set.of()).forEach(keyBinding -> keyBinding.timesPressed++);
    }

    /**
     * @author ItsFelix5
     * @reason To allow multiple keybindings to be bound to the same key
     */
    @Overwrite
    public static void setKeyPressed(InputUtil.Key key, boolean pressed) {
        KeybindHolder.KEY_TO_BINDINGS.getOrDefault(key, Set.of()).forEach(keyBinding -> keyBinding.setPressed(pressed));
    }

    @Redirect(method = "updateKeysByCode", at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V"))
    private static void clearMap(Map<?, ?> instance) {
        KeybindHolder.KEY_TO_BINDINGS.clear();
    }

    @Redirect(method = "updateKeysByCode", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static <K, V> V addBinding(Map<K, V> instance, K k, V v) {
        KeybindHolder.KEY_TO_BINDINGS.computeIfAbsent((InputUtil.Key) k, unused -> new HashSet<>()).add((KeyBinding) v);
        return v;
    }

    @Redirect(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At(value = "INVOKE", target =
            "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1))
    private Object add(Map<?, ?> instance, Object key, Object value) {
        return KeybindHolder.KEY_TO_BINDINGS.computeIfAbsent((InputUtil.Key) key, k -> new HashSet<>()).add((KeyBinding) value);
    }
}
