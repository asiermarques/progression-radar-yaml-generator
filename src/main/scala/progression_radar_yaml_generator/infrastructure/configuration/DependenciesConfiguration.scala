package progression_radar_yaml_generator.infrastructure.configuration

import cats.effect.IO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLParser.Feature
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.infrastructure.configuration.properties.JiraProperties
import progression_radar_yaml_generator.infrastructure.filesystem.FileWriter
import progression_radar_yaml_generator.infrastructure.formatter.yaml.CategoriesYamlFormatter
import progression_radar_yaml_generator.infrastructure.repository.JiraCategoryRepository
import progression_radar_yaml_generator.infrastructure.strategy.{SourceContext, SourceImplementation}
@EnableConfigurationProperties(Array(classOf[JiraProperties]))
@Configuration
class DependenciesConfiguration {

  @Bean
  var jiraProperties: JiraProperties = _

  @Bean
  def fileWriter: FileWriter[IO] = new FileWriter[IO]

  @Bean
  def objectMapper: ObjectMapper = new ObjectMapper(new YAMLFactory())

  @Bean
  def categoriesYamlFormatter(objectMapper: ObjectMapper) = new CategoriesYamlFormatter(objectMapper)

  @Bean
  def jiraCategoryRepository(jiraProperties: JiraProperties): JiraCategoryRepository[IO] =
    new JiraCategoryRepository[IO](
      new RestTemplate(),
      jiraProperties.username,
      jiraProperties.token,
      jiraProperties.project
    )

  @Bean
  def sourceStrategyContext(jiraCategoryRepository: JiraCategoryRepository[IO]): SourceContext[IO] = {
    val sourceContext = new SourceContext[IO]
    sourceContext.registerCategoryRepository(SourceImplementation.JIRA, jiraCategoryRepository)
    sourceContext
  }

  @Bean
  def generateUseCase(
    jiraCategoryRepository: JiraCategoryRepository[IO],
    categoriesYamlFormatter: CategoriesYamlFormatter
  ): GenerateCategoriesYamlUseCase[IO] =
    new GenerateCategoriesYamlUseCase[IO](fileWriter, categoriesYamlFormatter)

}
