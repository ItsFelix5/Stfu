//? > 1.21.8 {
/*package stfu.mixin.UnfocusedVolumeReducer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import stfu.config.Config;

import static stfu.Main.client;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @WrapMethod(method = "calculateVolume(FLnet/minecraft/sounds/SoundSource;)F")
    private float calculateVolume(float f, SoundSource soundSource, Operation<Float> original) {
        return original.call(f, soundSource) * (!client.isWindowActive()? Config.get().unfocusedVolume : 1F);
    }
}
*///?}