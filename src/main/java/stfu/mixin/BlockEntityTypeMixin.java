package stfu.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import stfu.Holder;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements Holder<BlockEntityRenderer<?>> {
    @Unique
    private BlockEntityRenderer<?> renderer;

    @Override
    public BlockEntityRenderer<?> stfu$get() {
        return renderer;
    }

    @Override
    public void stfu$set(BlockEntityRenderer<?> renderer) {
        this.renderer = renderer;
    }
}
