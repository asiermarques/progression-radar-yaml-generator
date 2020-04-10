name := "progression_radar_yaml_generator"

version := "0.1"

scalaVersion := "2.13.1"
val springBootVersion                            = "2.2.6.RELEASE"
def springBootModule(artifact: String): ModuleID = "org.springframework.boot" % artifact % springBootVersion

libraryDependencies ++= Seq(
  springBootModule("spring-boot-starter-web") exclude ("org.springframework.boot", "spring-boot-starter-tomcat"),
  springBootModule("spring-boot-configuration-processor"),
  "org.springframework.shell" % "spring-shell-starter" % "2.0.1.RELEASE",
  "net.jcazevedo"             %% "moultingyaml"        % "0.4.2",
  "org.typelevel"             %% "cats-core"           % "2.0.0",
  "org.typelevel"             %% "cats-effect"         % "2.1.2"
)
