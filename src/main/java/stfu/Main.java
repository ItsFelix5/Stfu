package stfu;

import com.mojang.blaze3d.audio.Channel;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client./*?if <26.1{*//*keybinding*//*?}else >>'.'*/keymapping.v1.KeyMappingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
//? >1.21.8
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import org.lwjgl.glfw.GLFW;
import stfu.config.Config;

import java.util.Map;

@Entrypoint("main")
public class Main implements ModInitializer {
    public static final Minecraft client = Minecraft.getInstance();
    public static final KeyMapping NARRATOR_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.stfu.narrator_hotkey",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            /*? < 1.21.9 {*//*"key.categories.misc"*//*?} else {*/KeyMapping.Category.MISC/*?}*/
    ));
    private static final KeyMapping SKIP_MUSIC_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.stfu.skip_music",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            /*? < 1.21.9 {*//*"key.categories.misc"*//*?} else {*/KeyMapping.Category.MISC/*?}*/
    ));
    private static final KeyMapping TOGGLE_MUSIC_KEY = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.stfu.toggle_music",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            /*? < 1.21.9 {*//*"key.categories.misc"*//*?} else {*/KeyMapping.Category.MISC/*?}*/
    ));
    public static boolean musicPaused = false;

    @Override
    public void onInitialize() {
        Config.HANDLER.load();
        //? >1.21.8
        DebugScreenEntries.register(Identifier.fromNamespaceAndPath("stfu", "fps_history"), new DebugEntryFpsHistory());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (SKIP_MUSIC_KEY.consumeClick()) {
                //$ overlay_message '"msg.stfu.skip_music"'
                client.player.sendOverlayMessage(Component.translatable("msg.stfu.skip_music"));
                client.getMusicManager().stopPlaying();
                client.getMusicManager().startPlaying(client.getSituationalMusic());
                musicPaused = false;
            }
            if (TOGGLE_MUSIC_KEY.consumeClick()) {
                musicPaused = !musicPaused;
                if (musicPaused) {
                    for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : client.getSoundManager().soundEngine.instanceToChannel.entrySet()) {
                        if (entry.getKey().getSource() == SoundSource.MUSIC) entry.getValue().execute(Channel::pause);
                    }
                    //$ overlay_message '"msg.stfu.pause_music"'
                    client.player.sendOverlayMessage(Component.translatable("msg.stfu.pause_music"));
                } else {
                    for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> entry : client.getSoundManager().soundEngine.instanceToChannel.entrySet()) {
                        if (entry.getKey().getSource() == SoundSource.MUSIC) entry.getValue().execute(Channel::unpause);
                    }
                    //$ overlay_message '"msg.stfu.resume_music"'
                    client.player.sendOverlayMessage(Component.translatable("msg.stfu.resume_music"));
                }
            }
            if (musicPaused) client.getMusicManager().nextSongDelay++;
        });
    }
}
