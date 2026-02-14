package stfu.mixin.DeleteToTrash;

import com.sun.jna.platform.FileUtils;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.awt.*;

@Mixin(WorldSelectionList.WorldListEntry.class)
public class WorldListEntryMixin {
    @ModifyConstant(method = "deleteWorld", constant = @Constant(stringValue = "selectWorld.deleteWarning"))
    private String deleteWarning(String warning) {
        return FileUtils.getInstance().hasTrash() || (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH)) ? "selectWorld.trashWarning" : warning;
    }
}
