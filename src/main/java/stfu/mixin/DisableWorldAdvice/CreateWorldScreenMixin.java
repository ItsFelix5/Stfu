package stfu.mixin.DisableWorldAdvice;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @WrapOperation(method = "tryApplyNewDataPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/flag/FeatureFlags;isExperimental(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"))
    private boolean isExperimental(FeatureFlagSet features, Operation<Boolean> original) {
        return !Config.get().disableWorldAdvice && original.call(features);
    }
}
