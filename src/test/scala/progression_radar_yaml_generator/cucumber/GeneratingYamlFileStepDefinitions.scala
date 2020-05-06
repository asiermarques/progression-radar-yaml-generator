package progression_radar_yaml_generator.cucumber

import java.io.{File, FileInputStream}
import java.net.URI

import cats.effect.IO
import io.cucumber.java.{After, Before, PendingException}
import io.cucumber.java.en.{Given, Then, When}
import org.junit.jupiter.api.AfterAll
import org.scalatest.Assertion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpMethod, HttpStatus, ResponseEntity}
import org.springframework.web.client.RestTemplate
import progression_radar_yaml_generator.{BaseIntegrationTest, BaseUnitSpec}
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.domain.CategoryRepository
import progression_radar_yaml_generator.fixture.CategoriesJiraResponseDTOGenerator
import progression_radar_yaml_generator.infrastructure.controller.shell.GenerateFileShellCommand
import progression_radar_yaml_generator.infrastructure.filesystem.FileWriter
import progression_radar_yaml_generator.infrastructure.formatter.yaml.CategoriesYamlFormatter
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.transformer.DtoToDomainEntityListTransformer
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.configuration.JiraProperties
import progression_radar_yaml_generator.infrastructure.source_strategy.{SourceContext, SourceImplementation}
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.JiraCategoryRepository
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.dto.CategoriesJiraResponseDTO.{
  CategoriesJiraIssueDTO,
  CategoriesJiraIssueFieldsDTO,
  CategoriesJiraIssueStatusDTO,
  CategoriesJiraResponseDTO
}

import scala.io.Source

import scala.jdk.CollectionConverters._

class GeneratingYamlFileStepDefinitions extends BaseIntegrationTest {

  @Autowired
  var useCase: GenerateCategoriesYamlUseCase[IO] = _
  val sourceContext: SourceContext[IO]           = new SourceContext[IO]
  val restTemplate: RestTemplate                 = mock[RestTemplate]
  var repository: CategoryRepository[IO]         = _
  val outputFile: File                           = File.createTempFile("_generation_yaml_file", ".yml")
  var shellCommand: GenerateFileShellCommand     = _
  var jiraProjectId: String                      = _
  var jiraData: CategoriesJiraResponseDTO        = _

  var result: String = _

  @Before
  def init(): Unit = ()

  @AfterAll
  def finish() = outputFile.delete()

  @Given("we have a Jira that has a project with the key {string}")
  def a_Jira_server_with_a_project_with_a_key(projectKey: String): Unit =
    jiraProjectId = projectKey

  @Given("the project has the following issues data:")
  def the_project_has_the_following_issues_data(javaList: java.util.List[java.util.Map[String, String]]): Unit =
    jiraData = CategoriesJiraResponseDTO(issues =
      javaList.asScala
        .map(_.asScala.toMap[String, String])
        .toList
        .map(issue =>
          CategoriesJiraIssueDTO(
            fields = CategoriesJiraIssueFieldsDTO(
              summary = issue("issue summary"),
              description = issue("description"),
              labels = Array(issue("label")),
              status = CategoriesJiraIssueStatusDTO(name = issue("status"))
            )
          )
        )
        .toArray
    )

  @Given("the project has no configured issues")
  def the_project_has_no_issues_configured(): Unit =
    jiraData = CategoriesJiraResponseDTO(issues = Array.empty[CategoriesJiraIssueDTO])

  @When("the user executes the generate command with the {string} source and the full path of the target file")
  def the_user_executes_the_generate_command_with_the_source_and_the_full_path_of_the_target_file(
    source: String
  ): Unit = {

    reset(restTemplate)
    setUpJiraRepository()

    doReturn(
      new ResponseEntity[CategoriesJiraResponseDTO](jiraData, HttpStatus.OK),
      new ResponseEntity[CategoriesJiraResponseDTO](
        CategoriesJiraResponseDTO(issues = Array.empty[CategoriesJiraIssueDTO]),
        HttpStatus.OK
      )
    ).when(restTemplate)
      .exchange(any[URI], any[HttpMethod], any[HttpEntity[String]], any[Class[CategoriesJiraResponseDTO]])

    shellCommand =
      new GenerateFileShellCommand(useCase, sourceContext, SourceImplementation.values.map(_.toString).toSeq)

    result = shellCommand.generate(source, outputFile.getAbsolutePath)
  }

  @Then("the client receives a success output with the path of the created file")
  def the_client_receives_the_path_of_the_created_file(): Unit =
    result shouldBe s"success:  ${outputFile.getAbsolutePath}"

  @Then("the file contains the following yaml content:")
  def the_file_contains_the_following_yaml_content(yamlContent: String): Unit =
    getFileContent(outputFile.getAbsolutePath) shouldBe yamlContent.trim

  private def getFileContent(path: String): String = {
    val source = Source.fromFile(outputFile.getAbsolutePath)
    val result = source.getLines.toList.mkString("\n")
    source.close
    result
  }

  private def setUpJiraRepository(): Unit = {

    repository = new JiraCategoryRepository[IO](restTemplate, new JiraProperties {
      username = "test"
      token = "test"
      project = jiraProjectId
      url = "https://test"
    }, DtoToDomainEntityListTransformer.transform)
    sourceContext.registerCategoryRepository(SourceImplementation.JIRA, repository)
  }

}
