package stfu.mixin.UnfocusedVolumeReducer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.config.Config;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    private SoundManager soundManager;

    @Shadow
    @Final
    public Options options;

    @Inject(method = "setWindowActive", at = @At("TAIL"))
    private void setWindowActive(boolean bl, CallbackInfo ci) {
        if(soundManager != null)
        //? if >1.21.10 {
            soundManager.refreshCategoryVolume(SoundSource.MASTER);
        //? }else
            //soundManager.updateSourceVolume(SoundSource.MASTER/*? if <=1.21.8{*//*, options.getSoundSourceVolume(SoundSource.MASTER) * (!bl? Config.get().unfocusedVolume : 1F)*//*?}*/);
    }
}