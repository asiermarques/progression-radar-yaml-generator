package progression_radar_yaml_generator

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ProgressionRadarYamlGeneratorApp

object ProgressionRadarYamlGeneratorApp extends App {
  SpringApplication run (classOf[ProgressionRadarYamlGeneratorApp], args: _*)
}
