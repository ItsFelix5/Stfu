package stfu.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IntegratedServerLoader.class)
abstract class IntegratedServerLoaderMixin {
    /**
     * @author Stfu
     * @reason To bypass the warning screen
     */
    @Overwrite
    public static void tryLoad(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader, boolean bypassWarnings) {
        loader.run();
    }

    @Redirect(method = "checkBackupAndStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SaveProperties;getLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private Lifecycle checkBackupAndStart(SaveProperties saveProperties) {
        return Lifecycle.stable();
    }
}
