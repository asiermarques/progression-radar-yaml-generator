package progression_radar_yaml_generator.cucumber

import cats.effect.IO
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.{Given, Then, When}
import org.scalatest.Assertion
import progression_radar_yaml_generator.BaseUnitSpec
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.infrastructure.controller.shell.GenerateFileShellCommand
import progression_radar_yaml_generator.infrastructure.filesystem.FileWriter
import progression_radar_yaml_generator.infrastructure.formatter.yaml.CategoriesYamlFormatter
import progression_radar_yaml_generator.infrastructure.source_strategy.SourceContext

import scala.jdk.CollectionConverters._

class ShowSourcesStepDefinitions extends BaseUnitSpec {

  val fileWriter: FileWriter[IO]                 = mock[FileWriter[IO]]
  val formatter: CategoriesYamlFormatter         = mock[CategoriesYamlFormatter]
  val useCase: GenerateCategoriesYamlUseCase[IO] = new GenerateCategoriesYamlUseCase[IO](fileWriter, formatter)
  val sourceContext: SourceContext[IO]           = mock[SourceContext[IO]]
  var shellCommand: GenerateFileShellCommand     = _

  var result: String = _

  @Given("the following implemented sources:")
  def the_following_implemented_sources(dataTable: DataTable): Unit =
    shellCommand = new GenerateFileShellCommand(useCase, sourceContext, dataTable.asList.asScala.toSeq)

  @Given("there are no implemented sources")
  def there_are_no_implemented_sources(): Unit =
    shellCommand = new GenerateFileShellCommand(useCase, sourceContext, Seq.empty[String])

  @When("the user executes the show-sources command")
  def the_user_executes_the_command(): Unit = result = shellCommand.showSources

  @Then("the client receives the terminal output {string}")
  def the_client_receives_the_terminal_output(output: String): Assertion =
    result shouldBe output

}
