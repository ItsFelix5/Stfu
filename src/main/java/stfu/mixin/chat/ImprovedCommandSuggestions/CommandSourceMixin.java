package stfu.mixin.chat.ImprovedCommandSuggestions;

import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandSource.class)
public interface CommandSourceMixin {
    @Redirect(method = "forEachMatching(Ljava/lang/Iterable;Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I"))
    private static int indexOf(String instance, int ch) {
        return 0;
    }

    @Inject(method = "shouldSuggest", at = @At("HEAD"), cancellable = true)
    private static void shouldSuggest(String remaining, String candidate, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(candidate.contains(remaining) || (remaining.indexOf(':') > 0 && candidate.contains(remaining.split(":", 2)[1])));
    }
}
