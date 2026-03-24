package stfu.mixin.rendering;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

@Mixin(Minecraft.class)
public abstract class DisableTextureAnimation {
    @WrapOperation(method = /*?if <26.1{*/"tick"/*?}else >>','*//*"runTick"*/, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;tick()V"))
    private void shouldTickTextures(TextureManager instance, Operation<Void> original) {
        if (Config.get().animateTextures) original.call(instance);
    }
}
