package stfu.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stfu.Config;

import static stfu.Main.client;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Shadow public boolean dirty;
    @Shadow private float flickerIntensity;
    @Unique private boolean hadNightVision;
    @Unique private long lastTimeOfDay;
    @Unique private long lastUpdate;
    @Unique private boolean hadConduitPower;
    @Unique private float lastSkyDarkness;
    @Unique private boolean appliedDarkness;
    @Unique private double lastGamma;

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private boolean isDirty() {
        // Time
        long timeDiff = Math.abs(lastTimeOfDay - lastUpdate);
        if(timeDiff >= 1870 || (timeDiff >= Config.get().lightmapUpdateDelay && (lastTimeOfDay < 731 || (lastTimeOfDay > 11270 && lastTimeOfDay < 13140) || lastTimeOfDay > 22861))) return true;

        // Conduit Power
        if((client.player.isSubmergedInWater() && client.player.hasStatusEffect(StatusEffects.CONDUIT_POWER)) != hadConduitPower) {
            hadConduitPower = !hadConduitPower;
            return true;
        }

        // Night Vision
        StatusEffectInstance nightVision = client.player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if(nightVision == null != hadNightVision) {
            hadNightVision = nightVision == null;
            return true;
        }
        if(Config.get().nightVisionFlicker && nightVision != null && nightVision.isDurationBelow(200)) return true;

        // Gamma
        if(!client.options.getGamma().getValue().equals(lastGamma)) {
            lastGamma = client.options.getGamma().getValue();
            return true;
        }

        // Darkness
        if ((client.options.getDarknessEffectScale().getValue() > 0 && client.player.hasStatusEffect(StatusEffects.DARKNESS)) != appliedDarkness) {
            appliedDarkness = !appliedDarkness;
            return true;
        }

        // Sky Darkness (Boss Events)
        if(client.gameRenderer.skyDarkness != lastSkyDarkness){
            lastSkyDarkness = client.gameRenderer.skyDarkness;
            return true;
        }

        return false;
    }

    @Inject(method = "enable", at = @At("TAIL"))
    private void enable(CallbackInfo ci) {
        if(client.world == null || client.player == null) return;

        if(isDirty()) {
            lastUpdate = client.world.getTimeOfDay() % 24000;

            this.flickerIntensity = (this.flickerIntensity + (float)((Math.random() - Math.random()) * Math.random() * Math.random() * 0.1)) * 0.9F;
            this.dirty = true;
        }
        lastTimeOfDay = client.world.getTimeOfDay() % 24000;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
     private void tick(CallbackInfo ci) {
         ci.cancel();
     }
}
