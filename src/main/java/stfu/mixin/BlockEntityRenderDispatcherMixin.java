package stfu.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.Holder;

import java.util.Map;

@SuppressWarnings("unchecked")
@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    /**
     * @author Stfu
     * @reason Optimization
     */
    @Overwrite
    @Nullable
    public <E extends BlockEntity> BlockEntityRenderer<E> get(E blockEntity) {
        return ((Holder<BlockEntityRenderer<E>>) blockEntity.getType()).stfu$get();
    }

    @Redirect(method = "reload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactories;reload(Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;)Ljava/util/Map;"))
    public Map<BlockEntityType<?>, BlockEntityRenderer<?>> reload(BlockEntityRendererFactory.Context context) {
        BlockEntityRendererFactories.FACTORIES.forEach((type, factory)-> ((Holder<BlockEntityRenderer<?>>) type).stfu$set(factory.create(context)));
        return null;
    }
}
