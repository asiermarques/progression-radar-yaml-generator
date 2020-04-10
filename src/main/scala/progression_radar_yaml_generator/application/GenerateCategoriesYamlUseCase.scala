package progression_radar_yaml_generator.application

import java.io.{File, PrintWriter}

import cats.effect.Sync
import cats.implicits._
import progression_radar_yaml_generator.domain.Category

class GenerateCategoriesYamlUseCase[F[_]: Sync] {

  def execute(findCategories: () => F[Seq[Category]], outputFile: String): F[String] =
    for {
      categories <- findCategories()
      yaml       <- generateYaml(categories)
      fileUrl    <- createFile(yaml, outputFile)
    } yield fileUrl

  def generateYaml(categories: Seq[Category]): F[String] = Sync[F].delay("")

  def createFile(yamlContent: String, outputFile: String): F[String] =
    Sync[F].delay {
      val pw = new PrintWriter(new File(outputFile))
      pw.write(yamlContent)
      pw.close
      outputFile
    }

}
