package progression_radar_yaml_generator.infrastructure.filesystem

import java.io.{File, PrintWriter}

import cats.effect.Sync

class FileWriter[F[_]: Sync] {

  def writeFile(content: String, outputFile: String): F[String] =
    Sync[F].delay {
      val pw = new PrintWriter(new File(outputFile))
      pw.write(content)
      pw.close
      outputFile
    }
}
