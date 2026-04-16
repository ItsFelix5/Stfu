plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.16-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "+" apply false
}

stonecutter active "26.1"

stonecutter parameters {
    replacements.string {
        direction = eval(current.version, ">1.21.9")
        replace("ResourceLocation", "Identifier")
    }
    replacements.string {
        direction = eval(current.version, ">1.21.11")
        replace("KeyBindingHelper", "KeyMappingHelper")
    }
    replacements.string {
        direction = eval(current.version, ">1.21.11")
        replace("registerKeyBinding", "registerKeyMapping")
    }
    replacements.string {
        direction = eval(current.version, ">1.21.11")
        replace("GuiGraphics", "GuiGraphicsExtractor")
    }
    swaps["overlay_message"] = when {
        eval(current.version, ">1.21.11") -> "client.player.sendOverlayMessage(Component.translatable($1));"
        else -> "client.player.displayClientMessage(Component.translatable($1), true);"
    }
}