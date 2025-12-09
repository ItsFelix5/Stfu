/*? <1.21.11 {*/package stfu.mixin.rendering.ModelGaps;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

import java.util.List;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
    @ModifyReturnValue(method = "createSideElements", at = @At("RETURN"))
    private static List<BlockElement> createSideElements(List<BlockElement> original) {
        if (Config.get().fixModelGaps) for (BlockElement e : original) {
            if (e.faces.size() == 1) {
                float fromX = e.from.x(), fromY = e.from.y();
                float toX = e.to.x(), toY = e.to.y();
                Direction dir = e.faces.keySet().stream().findAny().orElseThrow();
                float recess = dir == Direction.EAST || dir == Direction.DOWN? 0.0001F : -0.0001F;
                if (dir.getAxis() == Direction.Axis.Y) {
                    fromX -= 0.002F;
                    toX += 0.002F;
                    fromY += recess;
                    toY += recess;
                } else {
                    fromY += 0.002F;
                    toY -= 0.002F;
                    fromX += recess;
                    toX += recess;
                }
                ((Vector3f) e.from).set(fromX, fromY, e.from.z() - 0.002F);
                ((Vector3f) e.to).set(toX, toY, e.to.z() + 0.002F);
            }
        }
        return original;
    }

    @WrapOperation(method = "createOrExpandSpan", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ItemModelGenerator$Span;getAnchor()I"))
    private /*? > 1.21 {*/ /*static*//*?}*/ int createOrExpandSpan(ItemModelGenerator.Span instance, Operation<Integer> original, @Local(argsOnly = true) ItemModelGenerator.SpanFacing side, @Local(argsOnly = true, ordinal = 0) int i, @Local(argsOnly = true, ordinal = 1) int j) {
        if (Config.get().fixModelGaps && instance.getMax() != (side.isHorizontal() ? i : j) - 1) return -1;
        return original.call(instance);
    }
}
/*? } */