package progression_radar_yaml_generator.infrastructure.configuration

import cats.effect.IO
import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.infrastructure.configuration.properties.JiraProperties
import progression_radar_yaml_generator.infrastructure.filesystem.FileWriter
import progression_radar_yaml_generator.infrastructure.formatter.yaml.CategoriesYamlFormatter
import progression_radar_yaml_generator.infrastructure.repository.JiraCategoryRepository
import progression_radar_yaml_generator.infrastructure.strategy.{SourceContext, SourceImplementation}
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@EnableConfigurationProperties(Array(classOf[JiraProperties]))
@Configuration
class DependenciesConfiguration {

  @Bean
  def jacksonScalaModule(): Module = DefaultScalaModule

  @Bean
  var jiraProperties: JiraProperties = _

  @Bean
  def fileWriter: FileWriter[IO] = new FileWriter[IO]

  @Bean
  def objectMapper: ObjectMapper = {
    val mapper = new ObjectMapper(new YAMLFactory())
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
  @Bean
  def restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build

  @Bean
  def categoriesYamlFormatter(objectMapper: ObjectMapper) = new CategoriesYamlFormatter(objectMapper)

  @Bean
  def jiraCategoryRepository(jiraProperties: JiraProperties): JiraCategoryRepository[IO] =
    new JiraCategoryRepository[IO](new RestTemplate(), jiraProperties)

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
