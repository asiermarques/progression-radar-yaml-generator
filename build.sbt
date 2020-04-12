name := "progression_radar_yaml_generator"

version := "0.1"

scalaVersion := "2.13.1"

def springBootModule(artifact: String): ModuleID = "org.springframework.boot" % artifact % "2.2.6.RELEASE"

libraryDependencies ++= Seq(
  springBootModule("spring-boot-starter-web") exclude ("org.springframework.boot", "spring-boot-starter-tomcat"),
  springBootModule("spring-boot-configuration-processor"),
  "org.springframework.shell"        % "spring-shell-starter"    % "2.0.1.RELEASE",
  "net.jcazevedo"                    %% "moultingyaml"           % "0.4.2",
  "org.typelevel"                    %% "cats-core"              % "2.0.0",
  "org.typelevel"                    %% "cats-effect"            % "2.1.2",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.1.2",
  "com.fasterxml.jackson.module"     %% "jackson-module-scala"   % "2.10.3"
)

libraryDependencies ++= Seq(
  springBootModule("spring-boot-starter-test") % "test, it" exclude ("org.mockito", "mockito-core"),
  "org.scalatest"                              %% "scalatest" % "3.1.0" % "test, it",
  "org.mockito"                                %% "mockito-scala" % "1.13.5" % "test, it"
)
