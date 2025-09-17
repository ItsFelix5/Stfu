package stfu.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ServerMetadataS2CPacket;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

@Mixin(ClientPlayNetworkHandler.class)
abstract class PlayNetworkHandlerMixin {
    @Redirect(method = "onServerMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/ServerMetadataS2CPacket;isSecureChatEnforced()Z", ordinal = 1))
    private boolean isSecureChatEnforced(ServerMetadataS2CPacket instance) {
        return true;
    }

    @Redirect(
            method = "method_34011",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/Recipe;showNotification()Z")
    )
    private boolean disableRecipeToasts(Recipe<?> instance) {
        return Config.get().recipeToasts && instance.showNotification();
    }

    @Inject(method = "onAdvancements", at = @At("HEAD"), cancellable = true)
    private void disableAdvancementToasts(AdvancementUpdateS2CPacket packet, CallbackInfo ci) {
        if(!Config.get().advancementToasts) ci.cancel();
    }
}
