package stfu.mixin;

import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.TimeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.Config;

@Mixin(value = MinecraftClient.class, priority = 999)
public abstract class MinecraftClientMixin {
    @Shadow protected abstract boolean shouldTick();

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;initBackendSystem()Lnet/minecraft/util/TimeSupplier$Nanoseconds;"))
    private TimeSupplier.Nanoseconds initBackendSystem() {
        GLX._initGlfw();
        return System::nanoTime;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;shouldTick()Z", ordinal = 0))
    private boolean shouldTickTextures(MinecraftClient instance){
        return Config.get().animateTextures && shouldTick();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;shouldTick()Z", ordinal = 1))
    private boolean shouldTickParticles(MinecraftClient instance){
        return !Config.get().disableParticles && shouldTick();
    }
}
