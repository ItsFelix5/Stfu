package stfu.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Config;
import stfu.Main;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    @Shadow private int lightningTicksLeft;
    @Shadow @Final private MinecraftClient client;
    @Unique private long lastUpdate;
    @Unique private int biomeColor;
    @Unique private Vec3d skyColor;

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    /**
     * @author Stfu
     * @reason optimization
     */
    @Overwrite
    public int getLightningTicksLeft() {
        return lightningTicksLeft;
    }

    /**
     * @author Stfu
     * @reason optimization
     */
    @Overwrite
    public void setLightningTicksLeft(int lightningTicksLeft) {
        if(client.options.getHideLightningFlashes().getValue()) {
            if(lightningTicksLeft > 0 != this.lightningTicksLeft > 0) Main.skyDirty = client.gameRenderer.getLightmapTextureManager().dirty = true;
            this.lightningTicksLeft = lightningTicksLeft;
        }
    }

    @Inject(method = "getSkyColor", at = @At(value = "HEAD"), cancellable = true)
    public void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if(client.world == null) return;

        int color = getBiomeAccess().getBiomeForNoiseGen(BlockPos.ofFloored(cameraPos.subtract(2, 2, 2))).value().getSkyColor();
        long time = client.world.getTimeOfDay() % 24000;
        if(biomeColor != color || getBiomeAccess().getBiomeForNoiseGen(BlockPos.ofFloored(cameraPos.add(3, 3, 3))).value().getSkyColor() != color || Main.skyDirty
                || Math.abs(time - lastUpdate) >= 1802 || ((time < 133 || (time > 11868 && time < 13670) || time > 22331) && Math.abs(time - lastUpdate) >= Config.get().skyUpdateDelay)) {
            Main.skyDirty = false;
            lastUpdate = time;
            biomeColor = color;
            return;
        }
        cir.setReturnValue(skyColor);
    }

    @Inject(method = "getSkyColor", at = @At("RETURN"))
    private void setSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
            skyColor = cir.getReturnValue();
    }
}
