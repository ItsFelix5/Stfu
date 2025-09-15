package stfu.mixin;

import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.TimeSupplier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

@Mixin(value = MinecraftClient.class, priority = 999)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;
    @Shadow protected abstract boolean shouldTick();

    @Shadow @Final public GameRenderer gameRenderer;

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

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;"))
    private Overlay overlay(MinecraftClient instance){
        return null;
    }

    @Inject(method = "joinWorld", at = @At("HEAD"))
    private void joinWorld(ClientWorld world, CallbackInfo ci) {
        if(this.world == null || (world.getDimension() != this.world.getDimension())) gameRenderer.getLightmapTextureManager().dirty = true;
    }
}
