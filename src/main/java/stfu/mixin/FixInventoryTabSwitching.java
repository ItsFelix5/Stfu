package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class FixInventoryTabSwitching {
    @Shadow
    protected abstract void selectTab(CreativeModeTab group);

    @WrapOperation(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;checkTabClicked(Lnet/minecraft/world/item/CreativeModeTab;DD)Z"))
    private boolean checkTabClicked(CreativeModeInventoryScreen instance, CreativeModeTab group, double mouseX, double mouseY, Operation<Boolean> original) {
        if (original.call(instance, group, mouseX, mouseY)) {
            this.selectTab(group);
            return true;
        }
        return false;
    }

    @Redirect(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V"))
    private void selectTab(CreativeModeInventoryScreen instance, CreativeModeTab creativeModeTab) {
    }
}
