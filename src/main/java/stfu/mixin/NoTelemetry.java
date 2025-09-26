package stfu.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if > 1.21 {
import net.minecraft.client.session.telemetry.TelemetryManager;
import net.minecraft.client.session.telemetry.TelemetrySender;
//?} else {
/*import net.minecraft.client.util.telemetry.TelemetryManager;
import net.minecraft.client.util.telemetry.TelemetrySender;
*///?}

@Mixin(TelemetryManager.class)
abstract class NoTelemetry {
    @Inject(method = "getSender", at = @At("HEAD"), cancellable = true)
    private void NoopSender(CallbackInfoReturnable<TelemetrySender> cir) {
        cir.setReturnValue(TelemetrySender.NOOP);
    }
}
