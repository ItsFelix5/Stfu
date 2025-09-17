package stfu.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.tutorial.TutorialStep;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
abstract class GameOptionMixin {
    @Shadow
    @Final
    private SimpleOption<Boolean> operatorItemsTab;
    @Shadow
    @Final
    private SimpleOption<Boolean> realmsNotifications;

    @Inject(method = "load", at = @At("HEAD"))
    private void changeOptions(CallbackInfo ci) {
        GameOptions t = (GameOptions) (Object) this;
        t.onboardAccessibility = false;
        t.skipMultiplayerWarning = true;
        t.tutorialStep = TutorialStep.NONE;
        t.joinedFirstServer = true;
        operatorItemsTab.setValue(true);
        realmsNotifications.setValue(false);
    }
}
