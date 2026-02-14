package stfu.config;

import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class Config {
    public static final ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(/*? > 1.21 {*/Identifier.fromNamespaceAndPath/*?}else{*//*new Identifier*//*?}*/("stfu", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("stfu.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public static Config get(){
        return HANDLER.instance();
    }

    private static final String basic = "basic";
    private static final String advanced = "advanced";
    private static final String chat = "chat";
    private static final String loading = "loading";
    private static final String rendering = "rendering";

    @AutoGen(category = basic, group = chat)
    @IntSlider(min = 10, max = 5000, step = 10)
    @SerialEntry
    public int maxChatHistory = 100;

    @AutoGen(category = basic, group = chat)
    @Boolean
    @SerialEntry
    public boolean announceAdvancements = true;

    @AutoGen(category = basic)
    @Boolean
    @SerialEntry
    public boolean advancementToasts = true;

    @AutoGen(category = basic)
    @Boolean
    @SerialEntry
    public boolean recipeToasts = false;

    @AutoGen(category = basic, group = chat)
    @EnumCycler
    @SerialEntry
    public AdminChat adminChat = AdminChat.ENABLED;

    @AutoGen(category = basic, group = chat)
    @EnumCycler
    @SerialEntry
    public CompactChat compactChat = CompactChat.ONLY_CONSECUTIVE;

    @AutoGen(category = basic, group = loading)
    @Boolean
    @SerialEntry
    public boolean disableWidgetFade = true;

    @AutoGen(category = basic, group = loading)
    @Boolean
    @SerialEntry
    public boolean disableFade = false;

    @AutoGen(category = basic, group = loading)
    @Boolean
    @SerialEntry
    public boolean disableSplash = true;

    @AutoGen(category = basic, group = loading)
    @Boolean
    @SerialEntry
    public boolean disableLoadingTerrain = true;

    @AutoGen(category = basic, group = loading)
    @Boolean
    @SerialEntry
    public boolean disableWorldAdvice = true;

    /*? <1.21.11 {*//*@AutoGen(category = basic)
    @Boolean
    @SerialEntry
    public boolean fixModelGaps = true;*//*? } */

    @AutoGen(category = basic)
    @Boolean
    @SerialEntry
    public boolean nightVisionFlicker = false;

    @AutoGen(category = basic, group = rendering)
    @Boolean
    @SerialEntry
    public boolean disableParticles = false;

    @AutoGen(category = basic, group = rendering)
    @Boolean
    @SerialEntry
    public boolean animateTextures = true;

    @AutoGen(category = basic, group = rendering)
    @Boolean
    @SerialEntry
    public boolean renderWeather = true;

    @AutoGen(category = basic)
    @Boolean
    @SerialEntry
    public boolean combineBars = false;

    @AutoGen(category = basic)
    @Boolean
    @SerialEntry
    public boolean deleteToTrash = true;

    @AutoGen(category = basic)
    @FloatSlider(min = 0, max = 1, step = 0.01F)
    @SerialEntry
    public float unfocusedVolume = 1F;



    @AutoGen(category = advanced)
    @IntSlider(min = 1, max = 10, step = 1)
    @SerialEntry
    public int renderThreadPriority = (Runtime.getRuntime().availableProcessors() > 4) ? 8 : 5;

    @AutoGen(category = advanced)
    @IntSlider(min = 1, max = 10, step = 1)
    @SerialEntry
    public int serverThreadPriority = (Runtime.getRuntime().availableProcessors() > 4) ? 8 : 5;

    @AutoGen(category = advanced)
    @IntSlider(min = 1, max = 10, step = 1)
    @SerialEntry
    public int ioThreadPriority = 1;

    public enum AdminChat implements NameableEnum {
        ENABLED,
        ONLY_PLAYERS,
        DISABLED;

        @Override
        public Component getDisplayName() {
            return Component.translatable("yacl3.config.stfu:config.adminChat." + name().toLowerCase());
        }
    }

    public enum CompactChat implements NameableEnum {
        ALL,
        ONLY_CONSECUTIVE,
        NEVER;

        @Override
        public Component getDisplayName() {
            return Component.translatable("yacl3.config.stfu:config.compactChat." + name().toLowerCase());
        }
    }
}

