package stfu.mixin;

import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin {
    @Shadow
    private int selectionStart;

    @Shadow
    public abstract void setSelectionStart(int cursor);

    @Inject(method = "setMaxLength", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;onChanged" +
            "(Ljava/lang/String;)V"))
    private void setMaxLength(int length, CallbackInfo ci) {
        if (selectionStart > length) setSelectionStart(length);
    }
}
