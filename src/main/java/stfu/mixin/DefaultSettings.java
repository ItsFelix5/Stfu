package stfu.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.tutorial.TutorialSteps;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
abstract class DefaultSettings {
    @Shadow
    @Final
    private OptionInstance<Boolean> operatorItemsTab;
    @Shadow
    @Final
    private OptionInstance<Boolean> realmsNotifications;

    @Inject(method = "load", at = @At("HEAD"))
    private void changeOptions(CallbackInfo ci) {
        Options t = (Options) (Object) this;
        t.onboardAccessibility = false;
        t.skipMultiplayerWarning = true;
        t.tutorialStep = TutorialSteps.NONE;
        t.joinedFirstServer = true;
        operatorItemsTab.set(true);
        realmsNotifications.set(false);
    }
}
