package stfu.mixin.DeleteToTrash;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sun.jna.platform.FileUtils;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import stfu.config.Config;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Path;

@Mixin(LevelStorageSource.LevelStorageAccess.class)
public abstract class LevelStorageAccessMixin {
    @Shadow
    public abstract void close() throws IOException;

    @WrapOperation(method = "deleteLevel", at = @At(value = "INVOKE", target = "Ljava/nio/file/Files;walkFileTree(Ljava/nio/file/Path;Ljava/nio/file/FileVisitor;)Ljava/nio/file/Path;"))
    public Path deleteToTrash(Path path, FileVisitor<? super Path> visitor, Operation<Path> original) {
        if (Config.get().deleteToTrash) {
            try {
                close();
            } catch (IOException ignored) {}
            if (FileUtils.getInstance().hasTrash()) {
                System.out.println("Deleting with utils");
                try {
                    FileUtils.getInstance().moveToTrash(path.toFile());
                    System.out.println("Deleted with FileUtils");
                    return null;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH) && Desktop.getDesktop().moveToTrash(path.toFile())) {
                System.out.println("Deleted with Desktop");
                return null;
            }
            System.out.println("Failed to move to trash");
        }
        return original.call(path, visitor);
    }
}
