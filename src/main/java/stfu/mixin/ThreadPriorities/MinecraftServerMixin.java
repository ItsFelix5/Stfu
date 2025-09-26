package stfu.mixin.ThreadPriorities;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.config.Config;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Redirect(method = "startServer", at = @At(value = "INVOKE", target = "Ljava/lang/Runtime;availableProcessors()I"))
    private static int startServer(Runtime instance) {
        Thread.currentThread().setPriority(Config.get().serverThreadPriority);
        return 0;
    }
}
