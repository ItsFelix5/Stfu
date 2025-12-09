package stfu.mixin.rendering.ModelGaps;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

@Mixin(TextureAtlasSprite.class)
public abstract class SpriteMixin {
    @Shadow @Final private ResourceLocation atlasLocation;
    @Unique private static final ResourceLocation blockAtlas = /*? > 1.21 {*//*Identifier.fromNamespaceAndPath*//*?}else{*/new ResourceLocation/*?}*/("minecraft", "textures/atlas/blocks.png");

    @ModifyReturnValue(method = "uvShrinkRatio", at = @At("RETURN"))
    private float uvShrinkRatio(float original) {
        return Config.get().fixModelGaps && atlasLocation.equals(blockAtlas)? 0 : original;
    }
}
