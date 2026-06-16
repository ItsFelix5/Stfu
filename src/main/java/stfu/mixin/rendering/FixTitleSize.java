package stfu.mixin.rendering;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(/*?if<26.2{*//*Gui*//*?}else{*/net.minecraft.client.gui.Hud/*?}*/.class)
public abstract class FixTitleSize {
    @Shadow public abstract Font getFont();
    @Shadow private @Nullable Component title;
    @Shadow private @Nullable Component subtitle;

    @ModifyArgs(method = /*? < 1.21 {*//*"render"*//*?} else if <26.1 {*//*"renderTitle"*//*?} else {*/"extractTitle"/*?}*/, at = @At(value = "INVOKE", target = /*? < 1.21.8 {*//*"Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"*//*?} else {*/"Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;"/*?}*/, ordinal = 0))
    private void renderTitle(Args args, @Local(argsOnly = true) GuiGraphicsExtractor guiGraphics) {
        int titleWidth = getFont().width(this.title);
        int maxWidth = guiGraphics.guiWidth() - 16;

        if (titleWidth * 4 > maxWidth) {
            args.set(0, (float) maxWidth / titleWidth);
            args.set(1, (float) maxWidth / titleWidth);
        }
    }

    @ModifyArgs(method = /*? < 1.21 {*//*"render"*//*?} else if <26.1 {*//*"renderTitle"*//*?} else {*/"extractTitle"/*?}*/, at = @At(value = "INVOKE", target = /*? < 1.21.8 {*//*"Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"*//*?} else {*/"Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;"/*?}*/, ordinal = 1))
    private void renderSubTitle(Args args, @Local(argsOnly = true) GuiGraphicsExtractor guiGraphics) {
        int titleWidth = getFont().width(this.subtitle);
        int maxWidth = guiGraphics.guiWidth() - 16;

        if (titleWidth * 2 > maxWidth) {
            args.set(0, (float) maxWidth / titleWidth);
            args.set(1, (float) maxWidth / titleWidth);
        }
    }
}
