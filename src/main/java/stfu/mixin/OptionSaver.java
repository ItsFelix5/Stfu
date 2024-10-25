package stfu.mixin;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Options;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Mixin(GameOptions.class)
public class OptionSaver {
    @Shadow
    @Final
    static Logger LOGGER;
    @Shadow
    @Final
    static Gson GSON;
    @Shadow
    @Final
    private static Splitter COLON_SPLITTER;
    @Unique
    private final File optionsFile = FabricLoader.getInstance().getConfigDir().resolve("stfu.txt").toFile();

    @Unique
    private static <T> void load(SimpleOption<T> option, String value) {
        DataResult<T> dataResult = option.getCodec().parse(JsonOps.INSTANCE, JsonParser.parseReader(new JsonReader(new StringReader(value.isEmpty() ? "\"\"" : value))));
        dataResult.error().ifPresent(error -> LOGGER.error("Error parsing option value {} for option {}: {}", value, option, error.message()));
        dataResult.result().ifPresent(option::setValue);
    }

    @Unique
    private static <T> void write(SimpleOption<T> option, String key, PrintWriter printWriter) {
        option.getCodec()
                .encodeStart(JsonOps.INSTANCE, option.getValue()).get()
                .ifLeft(json -> printWriter.println(key + ':' + GSON.toJson(json)))
                .ifRight(error -> LOGGER.error("Error saving option {}: {}", option, error));
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void loadOptions(CallbackInfo ci) {
        if (!this.optionsFile.exists()) return;
        try (BufferedReader bufferedReader = Files.newReader(this.optionsFile, Charsets.UTF_8)) {
            bufferedReader.lines().forEach(line -> {
                try {
                    Iterator<String> iterator = COLON_SPLITTER.split(line).iterator();
                    String key = iterator.next();
                    if (!Options.OPTIONS.containsKey(key)) {
                        LOGGER.error("Unknown option: {}", key);
                        return;
                    }
                    load(Options.OPTIONS.get(key), iterator.next());
                } catch (Exception var3) {
                    LOGGER.warn("Skipping bad option: {}", line);
                }
            });
        } catch (Exception var7) {
            LOGGER.error("Failed to load options", var7);
        }
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void saveOptions(CallbackInfo ci) {
        try {
            optionsFile.getParentFile().mkdirs();
            optionsFile.createNewFile();
            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8))) {
                Options.OPTIONS.forEach((key, option) -> write(option, key, printWriter));
            }
        } catch (Exception var6) {
            LOGGER.error("Failed to save options", var6);
        }
    }
}
