package stfu.mixin.rendering.ModelGaps;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.model.json.GeneratedItemModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

import java.util.List;

@Mixin(GeneratedItemModel.class)
public class ModelMixin {
    @ModifyReturnValue(method = "addSubComponents", at = @At("RETURN"))
    private static List<ModelElement> addSubComponents(List<ModelElement> original) {
        if (Config.get().fixModelGaps) for (ModelElement e : original) {
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

    @WrapOperation(method = "buildCube(Ljava/util/List;Lnet/minecraft/client/render/model/json/GeneratedItemModel$Side;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/GeneratedItemModel$Frame;getLevel()I"))
    private /*? if > 1.21 {*/ static/*?}*/ int buildCube(GeneratedItemModel.Frame instance, Operation<Integer> original, @Local(argsOnly = true) GeneratedItemModel.Side side, @Local(argsOnly = true, ordinal = 0) int i, @Local(argsOnly = true, ordinal = 1) int j) {
        if (Config.get().fixModelGaps && instance.getMax() != (side.isVertical() ? i : j) - 1) return -1;
        return original.call(instance);
    }
}
