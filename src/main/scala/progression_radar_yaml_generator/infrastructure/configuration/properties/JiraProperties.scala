package progression_radar_yaml_generator.infrastructure.configuration.properties

import javax.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

import scala.beans.BeanProperty

@Validated
@ConfigurationProperties("jira")
class JiraProperties {

  @NotBlank
  @BeanProperty
  var url: String = _

  @NotBlank
  @BeanProperty
  var username: String = _

  @NotBlank
  @BeanProperty
  var token: String = _

  @NotBlank
  @BeanProperty
  var project: String = _

}
