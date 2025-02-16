package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class KeyBindingWidgetMixin {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isUnbound()Z"))
    private boolean shutReusedModifierKeys(KeyBinding instance, Operation<Boolean> original) {
        return original.call(instance) || instance.getCategory().equals(KeyBinding.CREATIVE_CATEGORY);
    }

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;equals(Lnet/minecraft/client/option/KeyBinding;)Z"))
    private boolean shutReusedModifierKeys(KeyBinding instance, KeyBinding other, Operation<Boolean> original) {
        return original.call(instance, other) && !other.getCategory().equals(KeyBinding.CREATIVE_CATEGORY)
                && !(instance.getCategory().equals(other.getCategory()) && instance.isDefault() && other.isDefault());
    }
}
