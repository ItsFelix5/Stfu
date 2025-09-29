package stfu.mixin.rendering;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;
//? if < 1.21 {
/*import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public abstract class DisableWeather {
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }

    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    private void tickRainSplashing(Camera camera, CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }
}
*///?} else {

import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticlesMode;

@Mixin(WeatherRendering.class)
public class DisableWeather {
    @Inject(method = "addParticlesAndSound", at = @At("HEAD"), cancellable = true)
    private void addParticlesAndSound(ClientWorld world, Camera camera, int ticks, ParticlesMode particlesMode, CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }

    @Inject(method = "renderPrecipitation*", at = @At("HEAD"), cancellable = true)
    private void renderPrecipitation(CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }
}
//?}