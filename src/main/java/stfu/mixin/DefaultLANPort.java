package stfu.mixin;

import net.minecraft./*? if < 1.21 {*//*client.*//*?}*/util.NetworkUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NetworkUtils.class)
public class DefaultLANPort {
    @ModifyConstant(method = "findLocalPort", constant = {@Constant(intValue = 0), @Constant(intValue = 25564)})
    private static int findLocalPort(int var1) {
        return 25565;
    }
}
