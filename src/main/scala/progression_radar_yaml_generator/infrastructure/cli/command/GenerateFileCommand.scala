package progression_radar_yaml_generator.infrastructure.cli.command

import cats.effect.IO
import javax.validation.constraints.Size
import org.springframework.shell.standard.{ShellComponent, ShellMethod, ShellOption}
import progression_radar_yaml_generator.application.GenerateCategoriesYamlUseCase
import progression_radar_yaml_generator.infrastructure.strategy.spring.validator.annotation.InSource
import progression_radar_yaml_generator.infrastructure.strategy.{SourceContext, SourceImplementation}

@ShellComponent
class GenerateFileCommand(
  generateUseCase: GenerateCategoriesYamlUseCase[IO],
  sourceStrategyContext: SourceContext[IO]
) {

  @ShellMethod("Generate the yaml from Jira project")
  def generate(
    @InSource
    @ShellOption(help = "the origin from the data (you can see all the origins with the show-origins command)")
    origin: String,
    @Size(min = 2)
    @ShellOption(help = "the output file absolute path url")
    outputFile: String
  ): String = {
    sourceStrategyContext.setSourceContext(SourceImplementation.withName(origin))
    generateUseCase
      .execute(sourceStrategyContext.getCategoryRepository.findAllCategories, outputFile)
      .attempt
      .unsafeRunSync() match {
      case Right(result)              => result
      case Left(exception: Exception) => s"error:  ${exception.getMessage}"
      case Left(_)                    => "error!"
    }
  }

  @ShellMethod("Show available origins")
  def showOrigins: String = SourceImplementation.values.toSeq.map(_.toString).reduce(_ + ", " + _)
}
