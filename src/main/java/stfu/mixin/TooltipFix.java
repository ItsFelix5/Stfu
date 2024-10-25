package stfu.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DrawContext.class)
public abstract class TooltipFix {
    @Unique
    private static final boolean disabled = FabricLoader.getInstance().isModLoaded("legacy");// #6
    @Shadow
    @Final
    private MatrixStack matrices;
    @Shadow
    @Final
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    public abstract int getScaledWindowWidth();

    @Shadow
    public abstract void draw();

    @Shadow
    public abstract int getScaledWindowHeight();

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void drawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        if (disabled) return;
        ci.cancel();
        if (components.isEmpty()) return;
        DrawContext self = (DrawContext) (Object) this;
        components = new ArrayList<>(components);

        int forcedWidth = 0;
        for (TooltipComponent component : components) {
            if (!(component instanceof OrderedTextTooltipComponent)) {
                int width2 = component.getWidth(textRenderer);
                if (width2 > forcedWidth)
                    forcedWidth = width2;
            }
        }

        int renderX = x + 12;
        int maxWidth = getScaledWindowWidth() - 20 - x;
        if (forcedWidth > maxWidth || maxWidth < 100) {
            maxWidth = x - 28;

            int maxX = 0;
            for (TooltipComponent component : components) {
                int newWidth = component.getWidth(textRenderer);
                if (newWidth > maxX) maxX = newWidth;
            }
            renderX -= 28 + maxX;
        }

        for (int i = 0; i < components.size(); i++) {
            if (!(components.get(i) instanceof OrderedTextTooltipComponent orderedTextTooltipComponent)) continue;
            MutableText text = Text.empty();

            orderedTextTooltipComponent.text.accept((index, style, codePoint) -> {
                text.append(Text.literal(new String(Character.toChars(codePoint))).setStyle(style));
                return true;
            });

            List<Text> children = text.getSiblings();
            for (int j = 0; j < children.size() - 1; j++) {
                String code = children.get(j).getString() + children.get(j + 1).getString();
                if (code.equals("\\n")) {
                    MutableText t1 = Text.literal("");
                    for (int k = 0; k < j; k++) t1.append(children.get(i));
                    MutableText t2 = Text.literal("");
                    for (int k = j + 2; k < children.size(); k++) t1.append(children.get(i));
                    components.set(i, TooltipComponent.of(t1.asOrderedText()));
                    components.add(i + 1, TooltipComponent.of(t2.asOrderedText()));
                    break;
                }
            }
        }
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i) instanceof OrderedTextTooltipComponent orderedTextTooltipComponent) {
                MutableText text = Text.empty();

                orderedTextTooltipComponent.text.accept((index, style, codePoint) -> {
                    text.append(Text.literal(new String(Character.toChars(codePoint))).setStyle(style));
                    return true;
                });
                if (text.getSiblings().isEmpty()) continue;

                components.remove(i);
                components.addAll(i, textRenderer.wrapLines(text, maxWidth).stream().map(TooltipComponent::of).toList());
            }
        }

        int i = 0;
        int j = components.size() == 1 ? -2 : 0;

        for (TooltipComponent component : components) {
            int k = component.getWidth(textRenderer);
            if (k > i) i = k;

            j += component.getHeight();
        }

        int renderY = y - 12;
        if (renderY + j + 3 > getScaledWindowHeight()) renderY = getScaledWindowHeight() - j - 3;
        this.matrices.push();
        this.draw();
        TooltipBackgroundRenderer.render(self, renderX, renderY, i, j, 400);
        this.draw();
        this.matrices.translate(0.0F, 0.0F, 400.0F);

        for (int r = 0; r < components.size(); ++r) {
            TooltipComponent tooltipComponent2 = components.get(r);
            tooltipComponent2.drawText(textRenderer, renderX, renderY, this.matrices.peek().getPositionMatrix(), this.vertexConsumers);
            tooltipComponent2.drawItems(textRenderer, renderX, renderY, self);
            renderY += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
        }

        this.matrices.pop();
    }
}
