//? if <= 1.21.8 {
/*package stfu.mixin.rendering;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static stfu.Main.client;

@Mixin(ParticleManager.class)
public class ParticleCulling {
//? if > 1.21 {
    @WrapWithCondition(method = "renderParticles(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/particle/ParticleTextureSheet;Ljava/util/Queue;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/particle/Particle.render(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"))
//?} else
//  @WrapWithCondition(method = "renderParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;buildGeometry(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"))

    private static boolean renderParticles(Particle instance, VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        return client.worldRenderer.frustum.isVisible(instance.getBoundingBox());
    }
}
*///?}