package stfu.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.*;
import stfu.Holder;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Shadow @Final private ItemModelManager itemModelManager;
    @Shadow @Final private MapRenderer mapRenderer;
    @Shadow @Final private BlockRenderManager blockRenderManager;
    @Shadow @Final private Supplier<LoadedEntityModels> entityModelsGetter;
    @Shadow @Final private EquipmentModelLoader equipmentModelLoader;
    @Shadow @Final private TextRenderer textRenderer;

    @Unique private EntityRenderer<AbstractClientPlayerEntity, ?> SLIM;
    @Unique private EntityRenderer<AbstractClientPlayerEntity, ?> WIDE;

    /**
     * @author Stfu
     * @reason Optimization
     */
    @Overwrite
    public <T extends Entity> EntityRenderer<? super T, ?> getRenderer(T entity) {
        if(entity instanceof AbstractClientPlayerEntity player) return (EntityRenderer<? super T, ?>) (player.getSkinTextures().model() == SkinTextures.Model.SLIM? SLIM:WIDE);
        return ((Holder<EntityRenderer<? super T, ?>>) entity.getType()).stfu$get();
    }

    /**
     * @author Stfu
     * @reason Optimization
     */
    @Overwrite
    public void reload(ResourceManager manager) {
        EntityRendererFactory.Context context = new EntityRendererFactory.Context(
                (EntityRenderDispatcher) (Object) this,
                itemModelManager,
                mapRenderer,
                blockRenderManager,
                manager,
                entityModelsGetter.get(),
                equipmentModelLoader,
                textRenderer
        );
        EntityRenderers.RENDERER_FACTORIES.forEach((type, factory) -> ((Holder<EntityRenderer<?, ?>>) type).stfu$set(factory.create(context)));
        EntityRenderers.PLAYER_RENDERER_FACTORIES.forEach((model, factory) -> {
            if(model == SkinTextures.Model.SLIM) SLIM = factory.create(context);
            else WIDE = factory.create(context);
        });
    }
}
