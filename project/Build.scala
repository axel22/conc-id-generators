
import sbt._
import Keys._
import Process._
import java.io.File



object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    name := "idgenerators",
    version := "0.1",
    scalaVersion := "2.10.1",
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies += "com.github.axel22" %% "scalameter" % "0.4-M2",
    testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    logBuffered := false
  )
}


object IdGeneratorsBuild extends Build {
  
  lazy val root = Project(
    "root",
    file("."),
    settings = BuildSettings.buildSettings
  )

}
