package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import stfu.Config;

@Mixin(Util.class)
public abstract class UtilMixin {
    @WrapOperation(method = {"method_27956", "method_28123"}, at = @At(value = "INVOKE", target = "Ljava/lang/Thread;setName(Ljava/lang/String;)V"))
    private static void wrapUncaughtExceptionHandler(Thread instance, String name, Operation<Void> original) {
        original.call(instance, name);
        instance.setPriority(Config.conf.ioThreadPriority);
    }
}