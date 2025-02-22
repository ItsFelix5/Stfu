package stfu.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.RecipeBookAddS2CPacket;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

@Mixin(ClientPlayNetworkHandler.class)
abstract class PlayNetworkHandlerMixin {
    @Redirect(method = "onGameJoin", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;displayedUnsecureChatWarning:Z", opcode = Opcodes.GETFIELD))
    private boolean onGameJoin(ClientPlayNetworkHandler instance) {
        return true;
    }

    @Redirect(
            method = "onRecipeBookAdd",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/RecipeBookAddS2CPacket$Entry;shouldShowNotification()Z")
    )
    private boolean disableRecipeToasts(RecipeBookAddS2CPacket.Entry instance) {
        return Config.conf.recipeToasts && instance.shouldShowNotification();
    }

    @Inject(method = "onAdvancements", at = @At("HEAD"), cancellable = true)
    private void disableAdvancementToasts(AdvancementUpdateS2CPacket packet, CallbackInfo ci) {
        if(!Config.conf.advancementToasts) ci.cancel();
    }
}
