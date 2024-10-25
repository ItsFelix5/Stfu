package stfu.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
abstract class ShutToasts {
    @Redirect(method = "onServerMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;add(Lnet/minecraft/client/toast/Toast;)V"))
    private void onServerMetadata(ToastManager instance, Toast toast) {
    }

    @Redirect(
            method = "method_34011",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;showNotification()Z")
    )
    private boolean doNotShowNotification(Recipe<?> instance) {
        return false;
    }
}
