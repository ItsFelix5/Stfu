package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.locale.Language;
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

@Mixin(GuiGraphics.class)
@DisableIf({"legacy", "legendarytooltips"})
public abstract class Tooltips {
    @Shadow public abstract int guiWidth();

    @ModifyVariable(method = /*? > 1.21 {*//*"renderTooltip"*//*?}else{*/"renderTooltipInternal"/*?}*/,
            at = @At("HEAD"), index = 2, argsOnly = true)
    private List<ClientTooltipComponent> wrapLines(List<ClientTooltipComponent> original) {
        ArrayList<ClientTooltipComponent> components = new ArrayList<>();

        for (ClientTooltipComponent tooltipComponent : original) {
            if (!(tooltipComponent instanceof ClientTextTooltip component)) {
                components.add(tooltipComponent);
                continue;
            }
            final int length = components.size();
            client.font.getSplitter().splitLines(new FormattedText() {
                @Override
                public <T> Optional<T> visit(ContentConsumer<T> visitor) {
                    return visit((s, t)->visitor.accept(t), Style.EMPTY);
                }

                @Override
                public <T> Optional<T> visit(StyledContentConsumer<T> visitor, Style s) {
                    component.text.accept((index, style, codePoint) -> visitor.accept(style.applyTo(s), new String(Character.toChars(codePoint))).isEmpty());
                    return Optional.empty();
                }
            }, guiWidth() - 12, Style.EMPTY, (t, lastLineWrapped) -> components.add(ClientTooltipComponent.create(Language.getInstance().getVisualOrder(t))));
            if (components.size() == length) components.add(ClientTooltipComponent.create(FormattedCharSequence.composite()));
        }

        return components;
    }

    @WrapOperation(method = /*? > 1.21 {*//*"renderTooltip"*//*?}else{*/"renderTooltipInternal"/*?}*/,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;"))
    private Vector2ic reposition(ClientTooltipPositioner instance, int screenWidth, int screenHeight, int mouseX, int mouseY, int width, int height, Operation<Vector2ic> original) {
        Vector2ic vector2ic = original.call(instance, screenWidth, screenHeight, mouseX, mouseY, width, height);
        int x = Math.max(6, Math.min(vector2ic.x(), screenWidth - width - 6));
        int y = Math.max(6, Math.min(vector2ic.y(), screenHeight - height - 6));
        if (x == 6 && y != 6 && width + 12 <= screenWidth) {
            x = Math.min(screenWidth - width - 6, Math.max(mouseX - width / 2, 6));
            y = mouseY - height - 12;

            if (y < 6) {
                if (screenHeight - mouseY > mouseY) y = mouseY + 12;
                else y = 6;
            }
        }
        return new Vector2i(x, y);
    }
}
