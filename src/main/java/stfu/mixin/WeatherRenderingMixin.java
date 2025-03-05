package stfu.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticlesMode;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

@Mixin(WeatherRendering.class)
public class WeatherRenderingMixin {
    @Inject(method = "addParticlesAndSound", at = @At("HEAD"), cancellable = true)
    private void addParticlesAndSound(ClientWorld world, Camera camera, int ticks, ParticlesMode particlesMode, CallbackInfo ci){
        if(!Config.get().renderWeather) ci.cancel();
    }

    @Inject(method = "renderPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    private void renderPrecipitation(World world, VertexConsumerProvider vertexConsumers, int ticks, float delta, Vec3d pos, CallbackInfo ci){
        if(!Config.get().renderWeather) ci.cancel();
    }
}
