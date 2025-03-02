package stfu.mixin;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.SharedConstants;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Main;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow protected abstract boolean processDebugKeys(int key);

    @ModifyConstant(method = "onKey", constant = @Constant(intValue = GLFW.GLFW_KEY_B))
    private int shutNarrator(int key) {
        return KeyBindingHelper.getBoundKeyOf(Main.NARRATOR_KEY).getCode();
    }

    @Inject(method = "processF3", at = @At("RETURN"), cancellable = true)
    private void processF3(int key, CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue() && SharedConstants.isDevelopment) cir.setReturnValue(processDebugKeys(key));
    }
}
