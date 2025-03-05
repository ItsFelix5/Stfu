package stfu.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import stfu.Holder;

@Mixin(EntityType.class)
public class EntityTypeMixin implements Holder<EntityRenderer<?, ?>> {
    @Unique
    private EntityRenderer<?, ?> renderer;

    @Override
    public EntityRenderer<?, ?> stfu$get() {
        return renderer;
    }

    @Override
    public void stfu$set(EntityRenderer<?, ?> renderer) {
        this.renderer = renderer;
    }
}