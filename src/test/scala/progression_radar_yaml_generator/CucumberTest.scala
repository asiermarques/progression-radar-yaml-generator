package progression_radar_yaml_generator

import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(features = Array("docs/features"), strict = true, monochrome = false, stepNotifications = true)
class CucumberTest
