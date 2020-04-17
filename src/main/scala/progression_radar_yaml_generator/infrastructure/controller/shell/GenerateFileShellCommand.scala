package progression_radar_yaml_generator.infrastructure.controller.shell

import cats.effect.IO
import javax.validation.constraints.Size
import org.springframework.shell.standard.{ShellComponent, ShellMethod, ShellOption}
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.infrastructure.source_strategy.{SourceContext, SourceImplementation}
import progression_radar_yaml_generator.infrastructure.spring.validator.annotation.InSource

@ShellComponent
class GenerateFileShellCommand(
  generateUseCase: GenerateCategoriesYamlUseCase[IO],
  sourceStrategyContext: SourceContext[IO],
  availableSources: Seq[String]
) {

  @ShellMethod("Generate the yaml from Jira project")
  def generate(
    @InSource
    @ShellOption(help = "the source from the data (you can see all the origins with the show-origins command)")
    source: String,
    @Size(min = 2)
    @ShellOption(help = "the output file absolute path url")
    outputFile: String
  ): String = {
    sourceStrategyContext.setSourceContext(SourceImplementation.withName(source))
    generateUseCase
      .execute(sourceStrategyContext.getCategoryRepository.findAllCategories, outputFile)
      .attempt
      .unsafeRunSync() match {
      case Right(result)              => s"success:  ${result}"
      case Left(exception: Exception) => s"error:  ${exception.getMessage}"
      case Left(_)                    => "unexpected error!"
    }
  }

  @ShellMethod("Show available sources")
  def showSources: String = availableSources.reduce(_ + ", " + _)
}
