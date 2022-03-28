plugins {
    kotlin("multiplatform") version "1.6.10"
}

val ktorVersion = "1.6.8"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-network:$ktorVersion")

            }
        }

        jvm().compilations["main"].defaultSourceSet {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-java:$ktorVersion")

            }
        }
    }
}