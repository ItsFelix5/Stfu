package stfu.mixin;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Main;

import java.util.Map;

import static stfu.Main.client;

@Mixin(SoundEngine.class)
public class PauseMusic {
    @Inject(method = "resume", at = @At("TAIL"))
    private void resume(CallbackInfo ci) {
        if(!Main.musicPaused) return;
        for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : client.getSoundManager().soundEngine.instanceToChannel.entrySet()) {
            if (entry.getKey().getSource() == SoundSource.MUSIC) entry.getValue().execute(Channel::pause);
        }
    }
}
