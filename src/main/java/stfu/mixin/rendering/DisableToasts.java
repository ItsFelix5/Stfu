package stfu.mixin.rendering;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import org.objectweb.asm.Opcodes;
//? > 1.21.1 {
//import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
//?} else {
import net.minecraft.world.item.crafting.Recipe;
//?}

@Mixin(ClientPacketListener.class)
abstract class DisableToasts {
    //? > 1.21 {
    @Redirect(method = "handleLogin", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;seenInsecureChatWarning:Z", opcode = Opcodes.GETFIELD))
    private boolean onGameJoin(ClientPacketListener instance) {
        return true;
    }
    //?} else {
/*  @Redirect(method = "handleServerData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;enforcesSecureChat()Z", ordinal = 1))
    private boolean isSecureChatEnforced(ClientboundServerDataPacket instance) {
        return true;
    }
*///?}
//? > 1.21.1 {
    /*@Redirect(
            method = "handleRecipeBookAdd",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundRecipeBookAddPacket$Entry;notification()Z")
    )
    private boolean disableRecipeToasts(ClientboundRecipeBookAddPacket.Entry instance) {
        return Config.get().recipeToasts && instance.notification();
    }*/
    //?} else {
    @Redirect(
            method = "method_34011",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Recipe;showNotification()Z")
    )
    private boolean disableRecipeToasts(Recipe<?> instance) {
        return Config.get().recipeToasts && instance.showNotification();
    }
//?}

    @Inject(method = "handleUpdateAdvancementsPacket", at = @At("HEAD"), cancellable = true)
    private void disableAdvancementToasts(ClientboundUpdateAdvancementsPacket packet, CallbackInfo ci) {
        if (!Config.get().advancementToasts) ci.cancel();
    }
}
