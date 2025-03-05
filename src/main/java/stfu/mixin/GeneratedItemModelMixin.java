package stfu.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.model.json.GeneratedItemModel;
import net.minecraft.client.render.model.json.ModelElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import stfu.Config;

import java.util.List;

@Mixin(GeneratedItemModel.class)
public class GeneratedItemModelMixin {
    @ModifyReturnValue(method = "addSubComponents", at = @At("RETURN"))
    private List<ModelElement> addSubComponents(List<ModelElement> original) {
        if(Config.get().fixModelGaps) for (var e : original) {
            if (e.faces.size() == 1) {
                float fromX = e.from.x(), fromY = e.from.y();
                float toX = e.to.x(), toY = e.to.y();
                switch (e.faces.keySet().stream().findAny().orElseThrow()) {
                    case UP -> {
                        fromX -= 0.002F;
                        fromY -= 0.0001F;
                        toX += 0.002F;
                        toY -= 0.0001F;
                    }
                    case DOWN -> {
                        fromX -= 0.002F;
                        fromY += 0.0001F;
                        toX += 0.002F;
                        toY += 0.0001F;
                    }
                    case WEST -> {
                        fromX -= 0.0001F;
                        fromY += 0.002F;
                        toX -= 0.0001F;
                        toY -= 0.002F;
                    }
                    case EAST -> {
                        fromX += 0.0001F;
                        fromY += 0.002F;
                        toX += 0.0001F;
                        toY -= 0.002F;
                    }
                }
                e.from.set(fromX, fromY, e.from.z() - 0.002F);
                e.to.set(toX, toY, e.to.z() + 0.002F);
            }
        }
        return original;
    }

    /**
     * @author Stfu
     * @reason To fix model gaps
     */
    @Overwrite
    private void buildCube(List<GeneratedItemModel.Frame> cubes, GeneratedItemModel.Side side, int x, int y) {
        int verticalX = side.isVertical() ? x : y;
        int verticalY = side.isVertical() ? y : x;
        for (GeneratedItemModel.Frame frame : cubes) {
            if (frame.getSide() == side && frame.getLevel() == verticalY && (!Config.get().fixModelGaps || frame.getMax() == verticalX - 1)) {
                frame.expand(verticalX);
                return;
            }
        }

        cubes.add(new GeneratedItemModel.Frame(side, verticalX, verticalY));
    }
}
