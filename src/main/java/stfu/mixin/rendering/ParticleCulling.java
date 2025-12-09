//? <= 1.21.8 {
package stfu.mixin.rendering;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static stfu.Main.client;

@Mixin(ParticleEngine.class)
public class ParticleCulling {
//? > 1.21 {
    /*@WrapWithCondition(method = "renderParticleType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
*///?} else
  @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))

    private static boolean renderParticles(Particle instance, VertexConsumer vertexConsumer, Camera camera, float v) {
        return client.levelRenderer.cullingFrustum.isVisible(instance.getBoundingBox());
    }
}
//?}