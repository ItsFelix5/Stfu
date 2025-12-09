package stfu;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import stfu.config.Config;

@Entrypoint("main")
public class Main implements ModInitializer {
    public static final Minecraft client = Minecraft.getInstance();
    public static final KeyMapping NARRATOR_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "options.narrator_hotkey",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            /*? < 1.21.9 {*/"key.categories.misc"/*?} else {*//*KeyMapping.Category.MISC*//*?}*/
    ));

    @Override
    public void onInitialize() {
        Config.HANDLER.load();
    }
}
