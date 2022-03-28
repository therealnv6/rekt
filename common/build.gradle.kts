plugins {
    kotlin("multiplatform")
}

val ktorVersion: String by project

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