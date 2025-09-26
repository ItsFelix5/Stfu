plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.11-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.8.+" apply false
}

stonecutter active "1.21.8"

stonecutter parameters {
    replacements.string {
        direction = eval(current.version, ">=1.21")
        replace("ItemModelGenerator", "GeneratedItemModel")
    }

    dependencies["fapi"] = node.project.property("deps.fabric_api") as String
    dependencies["yacl"] = node.project.property("deps.yacl") as String
    dependencies["modmenu"] = node.project.property("deps.modmenu") as String
}