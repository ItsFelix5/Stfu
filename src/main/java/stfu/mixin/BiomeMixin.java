package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(Biome.class)
public class BiomeMixin {
    @Unique
    private static ThreadLocal<Long2FloatLinkedOpenHashMap> staticTemperatureCache;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/ThreadLocal;withInitial(Ljava/util/function/Supplier;)Ljava/lang/ThreadLocal;"))
    private ThreadLocal<Long2FloatLinkedOpenHashMap> onBiomeInit(Supplier<Long2FloatLinkedOpenHashMap> supplier, Operation<ThreadLocal<Long2FloatLinkedOpenHashMap>> original) {
        if(staticTemperatureCache == null) staticTemperatureCache = original.call(supplier);
        return staticTemperatureCache;
    }
}
