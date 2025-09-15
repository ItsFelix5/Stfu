package stfu.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Config;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addToBatch", at = @At("HEAD"), cancellable = true)
    private void renderParticles(CallbackInfo ci) {
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
}
