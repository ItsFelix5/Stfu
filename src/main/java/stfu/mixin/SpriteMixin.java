package stfu.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import stfu.Config;

@Mixin(Sprite.class)
public abstract class SpriteMixin {
    @Shadow @Final private Identifier atlasId;
    @Unique private static final Identifier blockAtlas = Identifier.ofVanilla("textures/atlas/blocks.png");

    @ModifyReturnValue(method = "getFrameDeltaFactor", at = @At("RETURN"))
    private float getFrameDeltaFactor(float original) {
        return Config.get().fixModelGaps && atlasId.equals(blockAtlas)? 0 : original;
    }
}
