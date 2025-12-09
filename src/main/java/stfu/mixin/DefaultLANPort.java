package stfu.mixin;

import net.minecraft.util.HttpUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HttpUtil.class)
public class DefaultLANPort {
    @ModifyConstant(method = "getAvailablePort", constant = {@Constant(intValue = 0), @Constant(intValue = 25564)})
    private static int findLocalPort(int var1) {
        return 25565;
    }
}
