pluginManagement {
    repositories {
        google {
            content {
                // Incluye los grupos necesarios para Android y bibliotecas de Google
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral() // Repositorio de Maven Central
        gradlePluginPortal() // Repositorio de complementos de Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Repositorio de Android y Material Design
        mavenCentral() // Para bibliotecas adicionales
    }
}

// Configura el nombre del proyecto
rootProject.name = "TareaHenrik"

// Incluye el módulo principal (app)
include(":app")
