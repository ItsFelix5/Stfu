package stfu;

import com.google.common.collect.Maps;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.Map;
import java.util.Set;

public class KeybindHolder {
    public static final Map<InputUtil.Key, Set<KeyBinding>> KEY_TO_BINDINGS = Maps.newHashMap();
}
