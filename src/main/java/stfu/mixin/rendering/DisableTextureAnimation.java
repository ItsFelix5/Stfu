package stfu.mixin.rendering;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

@Mixin(MinecraftClient.class)
public abstract class DisableTextureAnimation {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;tick()V"))
    private void shouldTickTextures(TextureManager instance, Operation<Void> original) {
        if (Config.get().animateTextures) original.call(instance);
    }
}
