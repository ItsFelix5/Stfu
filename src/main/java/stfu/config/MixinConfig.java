package stfu.config;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

public class MixinConfig {
    private static boolean loaded = false;
    public static final ArrayList<String> DISABLED = new ArrayList<>();

    private static void load() {
        File file = FabricLoader.getInstance().getConfigDir().resolve("stfu-disable.txt").toFile();
        if (file.exists()) {
            try(BufferedReader reader = Files.newReader(file, Charsets.UTF_8)) {
                reader.lines().forEach(line -> {
                    if (!line.startsWith("#")) DISABLED.add(line);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        loaded = true;
    }

    public static boolean get(String name) {
        if (!loaded) load();
        return DISABLED.stream().anyMatch(name::startsWith);
    }
}
