package stfu.mixin.DisableWorldAdvice;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @WrapOperation(method = "applyDataPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/featuretoggle/FeatureFlags;isNotVanilla(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"))
    private boolean isNotVanilla(FeatureSet features, Operation<Boolean> original) {
        return !Config.get().disableWorldAdvice && original.call(features);
    }
}
