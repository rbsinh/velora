pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "velora"

include(
    ":app",
    ":core:model",
    ":core:domain",
    ":core:network",
    ":core:database",
    ":core:designsystem",
    ":core:analytics",
    ":core:health",
    ":feature:onboarding",
    ":feature:dashboard",
    ":feature:nutrition",
    ":feature:activity",
    ":feature:workout",
    ":feature:metrics",
    ":feature:profile",
)
