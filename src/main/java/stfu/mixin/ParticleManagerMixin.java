package stfu.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Inject(method = "renderParticles*", at = @At("HEAD"), cancellable = true)
    private void render(CallbackInfo ci) {
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

    @Inject(method = "addParticle*", at = @At("HEAD"), cancellable = true)
    private void addParticle(CallbackInfo ci) {
        if(Config.get().disableParticles) ci.cancel();
    }

    @Inject(method = "addBlockBreakingParticles*", at = @At("HEAD"), cancellable = true)
    private void addBlockBreakingParticles(CallbackInfo ci) {
        if(Config.get().disableParticles) ci.cancel();
    }
}
