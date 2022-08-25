import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs



version = "2022.04"

project {

    buildType(Router)
    buildType(DockerRouter)
    buildType(RouterDepoy)
    buildType(DeployServer)
}

object Router : BuildType({
    name = "Router"

    vcs {
        root(DslContext.settingsRoot, "+:Router",)
    }
    steps {
        script {
            name = "show file structure"
            scriptContent = """
                echo `pwd`
                echo `ls ./`
            """.trimIndent()

        }

    }

    triggers {
        vcs {
        }
    }
})

object DockerRouter : BuildType({
    name = "Docker"

    vcs {
        root(DslContext.settingsRoot, "+:DockerRouter")
    }
    dependencies {
        snapshot(Router) {

        }
    }
    triggers {
        vcs {

        }
    }
})

object RouterDepoy : BuildType ({
    name = "Router Deploy"
    vcs {
        root(DslContext.settingsRoot,
        "+:Router",
            "+:DockerRouter",
            "+:Deploy"
        )
    }
    dependencies {
        snapshot(DockerRouter) {

        }
    }
    triggers {
        vcs {

        }

    }
})

object DeployServer : BuildType ({
    name = "Deploy server"
    vcs {
        root(DslContext.settingsRoot, "+:Deployment")

    }
    dependencies {
        snapshot(RouterDepoy){
        }
    }

})
