//? > 1.21.6 {
package stfu.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import stfu.config.Config;

import static stfu.Main.client;

@Mixin(Gui.class)
public abstract class CombineBars {
    @Shadow
    protected abstract boolean willPrioritizeJumpInfo();

    @Inject(method = "nextContextualInfoState", at = @At("HEAD"), cancellable = true)
    private void getCurrentBarType(CallbackInfoReturnable<Gui.ContextualInfo> cir) {
        if (Config.get().combineBars) cir.setReturnValue(Gui.ContextualInfo.LOCATOR);
    }

    @WrapOperation(method = "renderHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;renderBackground(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
    private void renderBar(ContextualBarRenderer instance, GuiGraphics context, DeltaTracker tickCounter, Operation<Void> original) {
        if (!Config.get().combineBars) original.call(instance, context, tickCounter);
        else {
            LocalPlayer player = client.player;
            int nextLevelExperience = player.getXpNeededForNextLevel();
            PlayerRideableJumping mount = player.jumpableVehicle();
            int centerX = (client.getWindow().getGuiScaledWidth() - 182) / 2;
            int centerY = client.getWindow().getGuiScaledHeight() - 24 - 5;

            if (client.gameMode.hasExperience()) context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("hud/experience_bar_background"), centerX, centerY, 182, 5);
            else if (player.connection.getWaypointManager().hasWaypoints()) context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("hud/locator_bar_background"), centerX, centerY, 182, 5);
            else if (willPrioritizeJumpInfo()) context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("hud/jump_bar_background"), centerX, centerY, 182, 5);

            if (nextLevelExperience > 0 && player.experienceProgress > 0 && client.gameMode.hasExperience()) context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("hud/experience_bar_progress"),
                    182, 5, 0, 0, centerX, centerY, (int) (player.experienceProgress * 183), 5);

            if (mount != null) {
                if (mount.getJumpCooldown() > 0) context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("hud/jump_bar_cooldown"), centerX, centerY, 182, 5);
                else {
                    int progress = (int)(client.player.getJumpRidingScale() * 183F);
                    if (progress > 0) context.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("hud/jump_bar_progress"),
                            182, 5, 0, 0, centerX, centerY, progress, 5);
                }
            }
        }
    }
}
//?}