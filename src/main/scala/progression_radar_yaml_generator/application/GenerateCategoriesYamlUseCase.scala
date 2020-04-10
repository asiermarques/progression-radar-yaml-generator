package progression_radar_yaml_generator.application

import cats.effect.Sync
import cats.implicits._
import progression_radar_yaml_generator.domain.Category
import progression_radar_yaml_generator.infrastructure.filesystem.FileWriter

class GenerateCategoriesYamlUseCase[F[_]: Sync](fileWriter: FileWriter[F]) {

  def execute(findCategories: () => F[Seq[Category]], outputFile: String): F[String] =
    for {
      categories <- findCategories()
      yaml       <- generateYaml(categories)
      fileUrl    <- fileWriter.writeFile(yaml, outputFile)
    } yield fileUrl

  def generateYaml(categories: Seq[Category]): F[String] = Sync[F].delay("")

}
