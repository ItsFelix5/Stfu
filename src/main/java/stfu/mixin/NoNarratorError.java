package stfu.mixin;

import com.mojang.text2speech.Narrator;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Narrator.class)
public interface NoNarratorError {
    @Redirect(method = "getNarrator", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"))
    private static void noNarratorError(Logger instance, String s, Throwable throwable) {}

}
