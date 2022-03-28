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

        jvm().compilations["main"].defaultSourceSet {
            dependsOn(commonMain)
            dependencies {
                compileOnly("io.ktor:ktor-client-java:$ktorVersion")
            }
        }
    }
}