package stfu.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Config;
import stfu.Main;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    @Shadow private int lightningTicksLeft;
    @Shadow @Final private MinecraftClient client;
    @Unique private long lastUpdate;
    @Unique private int biomeColor;
    @Unique private int skyColor;

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
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
    public void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        if(client.world == null) return;

        int color = getBiomeAccess().getBiomeForNoiseGen(BlockPos.ofFloored(cameraPos.subtract(2))).value().getSkyColor();
        long time = client.world.getTimeOfDay() % 24000;
        if(biomeColor != color || getBiomeAccess().getBiomeForNoiseGen(BlockPos.ofFloored(cameraPos.add(3))).value().getSkyColor() != color) {
            Main.skyDirty = false;
            lastUpdate = time;
            biomeColor = color;
            return;
        } else if(Main.skyDirty || Math.abs(time - lastUpdate) >= 1802 || ((time < 133 || (time > 11868 && time < 13670) || time > 22331) && Math.abs(time - lastUpdate) >= Config.conf.skyUpdateDelay)) {
            Main.skyDirty = false;
            lastUpdate = time;

            updateSkyColor(Vec3d.unpackRgb(color).multiply(MathHelper.clamp(MathHelper.cos(
                    this.getSkyAngle(tickDelta) * (float) (Math.PI * 2)) * 2.0F + 0.5F, 0.0F, 1.0F)), tickDelta);
        }
        cir.setReturnValue(skyColor);
    }

    @Inject(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ColorHelper;getArgb(Lnet/minecraft/util/math/Vec3d;)I"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 2) Vec3d biomeVec) {
            updateSkyColor(biomeVec, tickDelta);
            cir.setReturnValue(skyColor);
    }

    @Unique
    private void updateSkyColor(Vec3d biomeVec, float tickDelta) {
        int red = MathHelper.floor(biomeVec.x * 255);
        int green = MathHelper.floor(biomeVec.y * 255);
        int blue = MathHelper.floor(biomeVec.z * 255);
        int gray = (int) ((float) red * 0.06F + (float) green * 0.118F + (float) blue * 0.022F);

        float rainGradient = this.getRainGradient(tickDelta);
        if (rainGradient > 0) {
            rainGradient *= 0.75F;
            red = MathHelper.lerp(rainGradient, red, gray * 3);
            green = MathHelper.lerp(rainGradient, green, gray * 3);
            blue = MathHelper.lerp(rainGradient, blue, gray * 3);
        }

        float thunderGradient = this.getThunderGradient(tickDelta);
        if (thunderGradient > 0) {
            thunderGradient *= 0.75F;
            red = MathHelper.lerp(thunderGradient, red, gray);
            green = MathHelper.lerp(thunderGradient, green, gray);
            blue = MathHelper.lerp(thunderGradient, blue, gray);
        }

        if (lightningTicksLeft > 0) {
            float delta = Math.min((float) lightningTicksLeft - tickDelta, 1.0F) * 0.45F;
            red = MathHelper.lerp(delta, red, 204);
            green = MathHelper.lerp(delta, green, 204);
            blue = MathHelper.lerp(delta, blue, 255);
        }

        skyColor = ColorHelper.getArgb(255, red, green, blue);
    }
}
