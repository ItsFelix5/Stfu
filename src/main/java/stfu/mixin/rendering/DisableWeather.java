package stfu.mixin.rendering;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;
//? < 1.21 {
/*import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;

@Mixin(LevelRenderer.class)
public abstract class DisableWeather {
    @Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
    private void renderWeather(LightTexture manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }

    @Inject(method = "tickRain", at = @At("HEAD"), cancellable = true)
    private void tickRainSplashing(Camera camera, CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }
}
*///?} else {

import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ParticleStatus;

@Mixin(WeatherEffectRenderer.class)
public class DisableWeather {
    @Inject(method = "tickRainParticles", at = @At("HEAD"), cancellable = true)
    private void addParticlesAndSound(ClientLevel clientLevel, Camera camera, int ticks, ParticleStatus particleStatus, /*? >= 1.21.11{*/ int j,/*?}*/ CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderPrecipitation(CallbackInfo ci){
        if (!Config.get().renderWeather) ci.cancel();
    }
}
//?}