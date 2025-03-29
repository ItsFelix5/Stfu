package stfu.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Main;

import static stfu.Main.client;

@Mixin(World.class)
public class WorldMixin {
    @Shadow protected float lastRainGradient;
    @Shadow protected float rainGradient;

    @Shadow protected float lastThunderGradient;
    @Shadow protected float thunderGradient;

    @Inject(method = "setRainGradient", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;rainGradient:F"))
    private void setRainGradient(float gradient, CallbackInfo ci) {
        if(lastRainGradient != rainGradient) Main.skyDirty = client.gameRenderer.getLightmapTextureManager().dirty = true;
    }

    @Inject(method = "setThunderGradient", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;thunderGradient:F"))
    private void setThunderGradient(float gradient, CallbackInfo ci) {
        if(lastThunderGradient != thunderGradient) Main.skyDirty = client.gameRenderer.getLightmapTextureManager().dirty = true;
    }
}
