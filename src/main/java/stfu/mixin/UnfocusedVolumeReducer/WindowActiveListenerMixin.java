package stfu.mixin.UnfocusedVolumeReducer;

import com.mojang.blaze3d.platform.Window;
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

import static stfu.Main.client;

@Mixin(/*?if <26.1{*/Minecraft/*?}else >>'.'*//*Window*/.class)
public class WindowActiveListenerMixin {
    /*?if <26.1{*/
    @Inject(method = "setWindowActive", at = @At("TAIL"))
    private void setWindowActive(boolean bl, CallbackInfo ci) {
        if(client.getSoundManager() != null)
        //? if >1.21.10 {
            /*client.getSoundManager().refreshCategoryVolume(SoundSource.MASTER);
        *///? }else
            client.getSoundManager().updateSourceVolume(SoundSource.MASTER/*? if <=1.21.8{*/, client.options.getSoundSourceVolume(SoundSource.MASTER) * (!bl? Config.get().unfocusedVolume : 1F)/*?}*/);
    }/*?}else{*/
    /*@Inject(method = "onFocus", at = @At("TAIL"))
    private void onFocus(long handle, boolean focused, CallbackInfo ci) {
        client.getSoundManager().refreshCategoryVolume(SoundSource.MASTER);
    }
    *//*?}*/
}