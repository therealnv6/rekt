plugins {
    kotlin("multiplatform")
}

val ktorVersion: String by project


kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(kotlin("stdlib-common"))
                compileOnly("io.ktor:ktor-client-core:$ktorVersion")
                compileOnly("io.ktor:ktor-network:$ktorVersion")
            }
        }

        val ktorIds = mapOf(
            jvm().compilations["main"].defaultSourceSet to "java",
            js().compilations["main"].defaultSourceSet to "js",

            // Could not resolve io.ktor:ktor-network:1.6.8., todo: fix
//            linuxX64().compilations["main"].defaultSourceSet to "curl",
//            mingwX64().compilations["main"].defaultSourceSet to "curl",
            macosX64().compilations["main"].defaultSourceSet to "ios"
        )

        for (ktorId in ktorIds)
        {
            val sourceSet = ktorId.key

            sourceSet.dependsOn(commonMain)
            sourceSet.dependencies {
                compileOnly("io.ktor:ktor-client-${ktorId.value}:$ktorVersion")
            }
        }
    }
}