package stfu.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Config;

import java.util.Map;
import java.util.Queue;

import static stfu.Main.client;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    private void renderParticles(CallbackInfo ci) {
        if(particles.isEmpty()) ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if(Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "addEmitter*", at = @At("HEAD"), cancellable = true)
    private void addEmitter(CallbackInfo ci) {
        if(Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void addParticle(CallbackInfo ci) {
        if(Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if(Config.get().disableParticles) cir.setReturnValue(null);
    }

    @Inject(method = "addBlockBreakingParticles*", at = @At("HEAD"), cancellable = true)
    private void addBlockBreakingParticles(CallbackInfo ci) {
        if(Config.get().disableParticles) ci.cancel();
    }

    @WrapWithCondition(method = "renderParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;buildGeometry(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"))
    private boolean renderParticles(Particle instance, VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Frustum frustum = client.worldRenderer.capturedFrustum;
        if(frustum == null) frustum = client.worldRenderer.frustum;
        return frustum.isVisible(instance.getBoundingBox());
    }
}
