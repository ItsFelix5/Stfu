package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.util.Language;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import stfu.DisableIf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static stfu.Main.client;

@Mixin(DrawContext.class)
@DisableIf({"legacy", "legendarytooltips"})
public abstract class DrawContextMixin {
    @Shadow public abstract int getScaledWindowWidth();

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
            at = @At("HEAD"), index = 2, argsOnly = true)
    private List<TooltipComponent> wrapLines(List<TooltipComponent> original) {
        ArrayList<TooltipComponent> components = new ArrayList<>();

        for (TooltipComponent tooltipComponent : original) {
            if (!(tooltipComponent instanceof OrderedTextTooltipComponent component)) {
                components.add(tooltipComponent);
                continue;
            }
            final int length = components.size();
            client.textRenderer.getTextHandler().wrapLines(new StringVisitable() {
                @Override
                public <T> Optional<T> visit(Visitor<T> visitor) {
                    return visit((s, t)->visitor.accept(t), Style.EMPTY);
                }

                @Override
                public <T> Optional<T> visit(StyledVisitor<T> visitor, Style s) {
                    component.text.accept((index, style, codePoint) -> visitor.accept(style.withParent(s), new String(Character.toChars(codePoint))).isEmpty());
                    return Optional.empty();
                }
            }, getScaledWindowWidth() - 12, Style.EMPTY, (t, lastLineWrapped) -> components.add(TooltipComponent.of(Language.getInstance().reorder(t))));
            if(components.size() == length) components.add(TooltipComponent.of(OrderedText.empty()));
        }

        return components;
    }

    @WrapOperation(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipPositioner;getPosition(IIIIII)Lorg/joml/Vector2ic;"))
    private Vector2ic reposition(TooltipPositioner instance, int screenWidth, int screenHeight, int mouseX, int mouseY, int width, int height, Operation<Vector2ic> original) {
        Vector2ic vector2ic = original.call(instance, screenWidth, screenHeight, mouseX, mouseY, width, height);
        int x = Math.max(6, Math.min(vector2ic.x(), screenWidth - width - 6));
        int y = Math.max(6, Math.min(vector2ic.y(), screenHeight - height - 6));
        if (x == 6 && y != 6) {
            x = Math.clamp(mouseX - width / 2, 6, screenWidth - 6);
            y = mouseY - height - 12;

            if (y < 6) y = mouseY + 12;
        }
        return new Vector2i(x, y);
    }
}
