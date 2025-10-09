package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeInventoryScreen.class)
public abstract class FixInventoryTabSwitching {
    @Shadow
    protected abstract void setSelectedTab(ItemGroup group);

    @WrapOperation(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;isClickInTab(Lnet/minecraft/item/ItemGroup;DD)Z"))
    private boolean isClickInTab(CreativeInventoryScreen instance, ItemGroup group, double mouseX, double mouseY, Operation<Boolean> original) {
        if (original.call(instance, group, mouseX, mouseY)) {
            this.setSelectedTab(group);
            return true;
        }
        return false;
    }

    @Redirect(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;isClickInTab(Lnet/minecraft/item/ItemGroup;DD)Z"))
    private boolean isClickInTab(CreativeInventoryScreen instance, ItemGroup group, double mouseX, double mouseY) {
        return false;
    }
}
