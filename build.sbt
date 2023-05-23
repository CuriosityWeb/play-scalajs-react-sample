ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.github.CuriosityWeb"

// JS Dependencies Version
lazy val circeVersion = "0.14.2"
lazy val scalajsDOMVersion = "2.2.0"
lazy val react4sVersion = "0.10.0-SNAPSHOT"
lazy val macroTaskExecutorVersion = "1.1.0"

// JVM Dependencies Version
lazy val guiceVersion = "5.1.0"
lazy val webjarsPlayVersion = "2.8.13"
lazy val reactVersion = "18.2.0"
lazy val h2Version = "2.1.214"
lazy val scalajsScriptsVersion = "1.2.0"
lazy val playSlickEvolutionVersion = "5.0.2"

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("./shared"))

lazy val sharedJS = shared.js.settings(
  libraryDependencies ++=
    Seq("io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion))

lazy val sharedJVM = shared.jvm.settings(
  libraryDependencies ++=
    Seq("io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion))

lazy val common = (project in file("./client/common"))
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJS)
  .settings(
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    libraryDependencies ++=
      Seq("org.scala-js" %%% "scalajs-dom" % scalajsDOMVersion,
        "com.github.ahnfelt" %%% "react4s" % react4sVersion,
        "org.scala-js" %%% "scala-js-macrotask-executor" % macroTaskExecutorVersion))

lazy val auth = (project in file("./client/auth"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(common)
  .settings(scalaJSUseMainModuleInitializer := true)

lazy val main = (project in file("./client/main"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(common)
  .settings(scalaJSUseMainModuleInitializer := true)

lazy val server = (project in file("server"))
  .enablePlugins(PlayScala)
  .dependsOn(sharedJVM)
  .settings(
    scalaJSProjects := Seq(auth, main),
    Assets / pipelineStages := Seq(scalaJSPipeline),
    libraryDependencies ++= Seq(
      guice,
      "com.google.inject" % "guice" % guiceVersion,
      "com.google.inject.extensions" % "guice-assistedinject" % guiceVersion,
      "org.webjars" %% "webjars-play" % webjarsPlayVersion,
      "org.webjars.npm" % "react" % reactVersion,
      "org.webjars.npm" % "react-dom" % reactVersion,
      "com.h2database" % "h2" % h2Version,
      "com.typesafe.play" %% "play-slick" % playSlickEvolutionVersion,
      "com.typesafe.play" %% "play-slick-evolutions" % playSlickEvolutionVersion,
      "com.vmunier" %% "scalajs-scripts" % scalajsScriptsVersion))
