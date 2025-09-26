//? if > 1.21.6 {
package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.JumpingMount;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

import static stfu.Main.client;

@Mixin(InGameHud.class)
public abstract class CombineBars {
    @Shadow
    protected abstract boolean shouldShowJumpBar();

    @Inject(method = "getCurrentBarType", at = @At("HEAD"), cancellable = true)
    private void getCurrentBarType(CallbackInfoReturnable<InGameHud.BarType> cir) {
        if (Config.get().combineBars) cir.setReturnValue(InGameHud.BarType.LOCATOR);
    }

    @WrapOperation(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/bar/Bar;renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    private void renderBar(Bar instance, DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (!Config.get().combineBars) original.call(instance, context, tickCounter);
        else {
            ClientPlayerEntity player = client.player;
            int nextLevelExperience = player.getNextLevelExperience();
            JumpingMount mount = player.getJumpingMount();
            int centerX = (client.getWindow().getScaledWidth() - 182) / 2;
            int centerY = client.getWindow().getScaledHeight() - 24 - 5;

            if (client.interactionManager.hasExperienceBar()) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/experience_bar_background"), centerX, centerY, 182, 5);
            else if (player.networkHandler.getWaypointHandler().hasWaypoint()) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/locator_bar_background"), centerX, centerY, 182, 5);
            else if (shouldShowJumpBar()) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/jump_bar_background"), centerX, centerY, 182, 5);

            if (nextLevelExperience > 0 && player.experienceProgress > 0 && client.interactionManager.hasExperienceBar()) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/experience_bar_progress"),
                    182, 5, 0, 0, centerX, centerY, (int) (player.experienceProgress * 183), 5);

            if (mount != null) {
                if (mount.getJumpCooldown() > 0) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/jump_bar_cooldown"), centerX, centerY, 182, 5);
                else {
                    int progress = (int)(client.player.getMountJumpStrength() * 183F);
                    if (progress > 0) context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/jump_bar_progress"),
                            182, 5, 0, 0, centerX, centerY, progress, 5);
                }
            }
        }
    }
}
//?}