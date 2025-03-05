package stfu.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(LanguageOptionsScreen.class)
public class LanguageOptionsScreenMixin {
    @Redirect(method = "onDone", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reloadResources()Ljava/util/concurrent/CompletableFuture;"))
    public CompletableFuture<Void> onReload(MinecraftClient client) {
        client.getLanguageManager().reload(client.getResourceManager());
        return null;
    }
}
