package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.DisableIf;

import java.util.ArrayList;
import java.util.List;

@Mixin(DrawContext.class)
@DisableIf({"legacy", "legendarytooltips"})
public abstract class DrawContextMixin {
    @Shadow public abstract int getScaledWindowWidth();

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At("HEAD"), index = 2, argsOnly = true)
    private List<TooltipComponent> makeComponentsMutable(List<TooltipComponent> components) {
        return new ArrayList<>(components);
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At("HEAD"))
    private void wrapComponents(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        if (components.isEmpty()) return;
        int maxWidth = getScaledWindowWidth() - 7;
        for (int i = 0; i < components.size(); i++) {
            if(!(components.get(i) instanceof OrderedTextTooltipComponent component)) continue;
            MutableText text = Text.empty();
            component.text.accept((index, style, codePoint) -> {
                text.append(Text.literal(new String(Character.toChars(codePoint))).setStyle(style));
                return true;
            });
            components.remove(i);
            components.addAll(i, textRenderer.wrapLines(text, maxWidth).stream().map(TooltipComponent::of).toList());
        }
    }

    @WrapOperation(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;"))
    private Vector2ic reposition(TooltipPositioner instance, int screenWidth, int screenHeight, int mouseX, int mouseY, int width, int height, Operation<Vector2ic> original) {
        Vector2ic vector2ic = original.call(instance, screenWidth, screenHeight, mouseX, mouseY, width, height);
        int x = Math.max(6, Math.min(vector2ic.x(), screenWidth - width - 6));
        int y = Math.max(6, Math.min(vector2ic.y(), screenHeight - height - 6));
        if (x == 6 && y != 6) {
            x = Math.clamp(mouseX - width / 2, 6, screenWidth - width - 6);
            y = mouseY - height - 12;

            if (y < 6) y = mouseY + 12;
        }
        return new Vector2i(x, y);
    }
}
