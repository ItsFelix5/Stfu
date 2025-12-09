package stfu.mixin;

import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import stfu.config.Config;

@Mixin(TitleScreen.class)
public class RemoveWidgetFade {
    @ModifyVariable(method = "<init>(ZLnet/minecraft/client/gui/components/LogoRenderer;)V", argsOnly = true, ordinal = 0, at = @At("HEAD"))
    private static boolean shutLoadFade(boolean bl) {
        return !Config.get().disableWidgetFade && bl;
    }
}
