package stfu.mixin;

import net.minecraft.client.main.Main;
import net.minecraft.util.crash.CrashMemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import stfu.Config;

@Mixin(Main.class)
public class MainMixin {
    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;initCrashReport()V"))
    private static void initCrashReport() {
        Thread.currentThread().setPriority(Config.get().renderThreadPriority);
        CrashMemoryReserve.reserveMemory();
    }
}
