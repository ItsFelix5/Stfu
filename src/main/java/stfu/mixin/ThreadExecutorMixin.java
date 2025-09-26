package stfu.mixin;

import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.locks.LockSupport;

@Mixin(ThreadExecutor.class)
public class ThreadExecutorMixin {
    @Inject(method = "waitForTasks", at = @At("HEAD"), cancellable = true)
    public void waitForTasks(CallbackInfo ci) {
        ci.cancel();
        LockSupport.parkNanos("waiting for tasks", 500000L);
    }
}
