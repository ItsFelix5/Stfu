//? >1.21.8 {
package stfu;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Locale;

import static stfu.Main.client;

@Environment(EnvType.CLIENT)
public class DebugEntryFpsHistory implements DebugScreenEntry {
    private static final ArrayDeque<Integer> history = new ArrayDeque<>(1200);

    static {
        ClientTickEvents.START_CLIENT_TICK.register(client -> history.add(client.getFps()));
    }

    @Override
    public void display(DebugScreenDisplayer debugScreenDisplayer, Level level, LevelChunk levelChunk, LevelChunk levelChunk2) {
        debugScreenDisplayer.addPriorityLine(
                String.format(Locale.ROOT, "%d fps (%d min %d avg %d max)", client.getFps(), Collections.min(history), history.stream().reduce(Integer::sum).orElseThrow() / history.size(), Collections.max(history))
        );
    }

    @Override
    public boolean isAllowed(boolean bl) {
        return true;
    }
}
//?}