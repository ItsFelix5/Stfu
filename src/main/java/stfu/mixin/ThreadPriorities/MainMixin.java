package stfu.mixin.ThreadPriorities;

import net.minecraft.client.main.Main;
import net.minecraft.util.MemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.config.Config;

@Mixin(Main.class)
public class MainMixin {
    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/CrashReport;preload()V"))
    private static void preload() {
        Thread.currentThread().setPriority(Config.get().renderThreadPriority);
        MemoryReserve.allocate();
    }
}
