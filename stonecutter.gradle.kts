plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "+" apply false
    id("me.modmuss50.mod-publish-plugin") version "+" apply false
}

stonecutter active "1.21.11"

stonecutter parameters {
    replacements.string {
        direction = eval(current.version, ">1.21.9")
        replace("ResourceLocation", "Identifier")
    }
}