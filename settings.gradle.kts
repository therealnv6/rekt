pluginManagement {
    this.resolutionStrategy {
        this.eachPlugin {
            if (this.requested.id.id == "org.jetbrains.kotlin.multiplatform")
            {
                this.useVersion("1.6.10")
            }
        }
    }
}

rootProject.name = "rekt-mp"

include("common")
include("serialization")
