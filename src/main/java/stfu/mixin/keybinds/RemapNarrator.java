/*? <1.21.11 {*/package stfu.mixin.keybinds;

import net.minecraft.client.KeyboardHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import stfu.Main;

@Mixin(KeyboardHandler.class)
public abstract class RemapNarrator {
    @ModifyConstant(method = "keyPress", constant = @Constant(intValue = GLFW.GLFW_KEY_B))
    private int shutNarrator(int key) {
        return Main.NARRATOR_KEY.key.getValue();
    }
}
/*? } */