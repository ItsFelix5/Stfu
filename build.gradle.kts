plugins {
    kotlin("jvm") version "2.2.21"
    id("com.google.devtools.ksp") version "2.3.3"
    `maven-publish`
    id("fabric-loom")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

version = "${property("mod.version")}-${stonecutter.current.version}"
group = "io.github.itsfelix5"
base.archivesName = "Stfu"

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    maven("https://maven.isxander.dev/releases")
    strictMaven("https://maven.terraformersmc.com/", "Terraformers MC", "com.terraformersmc")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:+")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}-fabric")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
}

val accessWidener = stonecutter.current.version + ".accesswidener"

loom {
    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
    }

    runConfigs.all {
        if (environment == "client") ideConfigGenerated(true)
        runDir = "../../run"
    }

    accessWidenerPath = file("../../src/main/resources/$accessWidener")

    runs {
        afterEvaluate {
            val mixinJarFile = configurations.runtimeClasspath.get().incoming.artifactView {
                componentFilter {
                    it is ModuleComponentIdentifier && it.group == "net.fabricmc" && it.module == "sponge-mixin"
                }
            }.files.first()

            configureEach {
                //vmArg("-javaagent:$mixinJarFile")
                vmArg("-XX:+AllowEnhancedClassRedefinition")
                vmArg("-DMC_DEBUG_ENABLED")
                vmArg("-DMC_DEBUG_HOTKEYS")
                vmArg("-DMC_DEBUG_VERBOSE_COMMAND_ERRORS")
                vmArg("-DMC_DEBUG_DEV_COMMANDS")
            }
        }
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "stfu.mixins.json") {
            env("CLIENT")
        }
    }

    lang.create("main") {
        patterns.add("assets/stfu/lang/**")
    }
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(property("deps.minecraft") as String, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks {
    processResources {
        inputs.property("version", project.property("mod.version"))

        filesMatching("fabric.mod.json") { expand(mapOf(
            "version" to project.property("mod.version"),
            "accesswidener" to accessWidener,
        )) }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile }, remapSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${property("mod.version")} for ${stonecutter.current.version}"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.addAll("fabric", "quilt")

    dryRun = !providers.environmentVariable("MODRINTH_TOKEN").isPresent
            || !providers.environmentVariable("CURSEFORGE_TOKEN").isPresent

    modrinth {
        projectId = "Rg9WdvvR"
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll((property("version.targets") as String).split(" "))

        projectDescription = providers.fileContents(layout.projectDirectory.file("readme.md")).asText

        requires("fabric-api")
        requires("yacl")
        optional("modmenu")
    }

    curseforge {
        projectId = "1111802"
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll((property("version.targets") as String).split(" "))

        requires("fabric-api")
        requires("yacl")
        optional("modmenu")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "io.github.itsfelix5.stfu"
            artifactId = property("mod.version") as String
            version = stonecutter.current.version

            from(components["java"])
        }
    }
}