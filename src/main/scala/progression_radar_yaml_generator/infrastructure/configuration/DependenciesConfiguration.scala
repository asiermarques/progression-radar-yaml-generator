package progression_radar_yaml_generator.infrastructure.configuration

import cats.effect.IO
import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.infrastructure.filesystem.FileWriter
import progression_radar_yaml_generator.infrastructure.formatter.yaml.CategoriesYamlFormatter
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.DtoToDomainEntityListTransformer.{
  transform => jiraDtoToEntityListTransformer
}
import progression_radar_yaml_generator.infrastructure.source_strategy.{SourceContext, SourceImplementation}
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.configuration.JiraProperties
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.JiraCategoryRepository

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
    val YAMLFactory = new YAMLFactory
    YAMLFactory.configure(Feature.MINIMIZE_QUOTES, true)
    YAMLFactory.configure(Feature.LITERAL_BLOCK_STYLE, true)
    YAMLFactory.configure(Feature.WRITE_DOC_START_MARKER, false)
    val mapper = new ObjectMapper(YAMLFactory)
    mapper.registerModule(DefaultScalaModule)
    mapper
  }

  @Bean
  def restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build

  @Bean
  def categoriesYamlFormatter(objectMapper: ObjectMapper) = new CategoriesYamlFormatter(objectMapper)

  @Bean
  def jiraCategoryRepository(restTemplate: RestTemplate, jiraProperties: JiraProperties): JiraCategoryRepository[IO] =
    new JiraCategoryRepository[IO](restTemplate, jiraProperties, jiraDtoToEntityListTransformer)

  @Bean
  def sourceStrategyContext(jiraCategoryRepository: JiraCategoryRepository[IO]): SourceContext[IO] = {
    val sourceContext = new SourceContext[IO]
    sourceContext.registerCategoryRepository(SourceImplementation.JIRA, jiraCategoryRepository)
    sourceContext
  }

  @Bean
  def generateUseCase(
    fileWriter: FileWriter[IO],
    categoriesYamlFormatter: CategoriesYamlFormatter
  ): GenerateCategoriesYamlUseCase[IO] =
    new GenerateCategoriesYamlUseCase[IO](fileWriter, categoriesYamlFormatter)

  @Bean
  def availableSources: Seq[String] = SourceImplementation.values.toSeq.map(_.toString)

}
