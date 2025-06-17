package stfu.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.Holder;

import java.util.Map;

@SuppressWarnings("unchecked")
@Mixin(value = EntityRenderDispatcher.class, priority = 999)
public class EntityRenderDispatcherMixin {
    @Shadow private Map<EntityType<?>, EntityRenderer<?, ?>> renderers;
    @Shadow private Map<SkinTextures.Model, EntityRenderer<? extends PlayerEntity, ?>> modelRenderers;
    @Unique private EntityRenderer<? extends PlayerEntity, ?> SLIM;
    @Unique private EntityRenderer<? extends PlayerEntity, ?> WIDE;

    @Inject(method = "getRenderer(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/render/entity/EntityRenderer;", at = @At("HEAD"), cancellable = true)
    public <T extends Entity> void getRenderer(T entity, CallbackInfoReturnable<EntityRenderer<? super T, ?>> cir) {
        if(entity instanceof AbstractClientPlayerEntity player) cir.setReturnValue((EntityRenderer<? super T, ?>) (player.getSkinTextures().model() == SkinTextures.Model.SLIM? SLIM:WIDE));
        else cir.setReturnValue(((Holder<EntityRenderer<? super T, ?>>) entity.getType()).stfu$get());
    }

    @Inject(method = "getRenderer(Lnet/minecraft/client/render/entity/state/EntityRenderState;)Lnet/minecraft/client/render/entity/EntityRenderer;", at = @At("HEAD"), cancellable = true)
    public <S extends EntityRenderState> void getRenderer(S state, CallbackInfoReturnable<EntityRenderer<?, ? super S>> cir) {
        if(state instanceof PlayerEntityRenderState player) cir.setReturnValue((EntityRenderer<AbstractClientPlayerEntity, S>) (player.skinTextures.model() == SkinTextures.Model.SLIM? SLIM:WIDE));
        else cir.setReturnValue(((Holder<EntityRenderer<? extends Entity, S>>) state.entityType).stfu$get());
    }

    @Inject(method = "reload", at = @At(value = "TAIL"))
    public void reload(ResourceManager manager, CallbackInfo ci, @Local EntityRendererFactory.Context context) {
        renderers.forEach((type,renderer)->((Holder<EntityRenderer<?, ?>>) type).stfu$set(renderer));
        modelRenderers.forEach((model,renderer)->{
            if(model == SkinTextures.Model.SLIM) SLIM = renderer;
            else WIDE = renderer;
        });
    }
}
