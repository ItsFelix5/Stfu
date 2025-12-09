package stfu.mixin.rendering;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class DisableParticles {
    @Shadow @Final private Map<ParticleRenderType, Queue<Particle>> particles;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderParticles(CallbackInfo ci) {
        if (particles.isEmpty() || Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "createTrackingEmitter*", at = @At("HEAD"), cancellable = true)
    private void addEmitter(CallbackInfo ci) {
        if (Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void addParticle(CallbackInfo ci) {
        if (Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void addParticle(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if (Config.get().disableParticles) cir.setReturnValue(null);
    }
}
