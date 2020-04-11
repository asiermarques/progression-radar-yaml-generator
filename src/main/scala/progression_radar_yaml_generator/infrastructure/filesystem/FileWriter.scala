package progression_radar_yaml_generator.infrastructure.filesystem

import java.io.{BufferedWriter, File, FileWriter => JavaFileWriter}
import cats.effect.Sync
import cats.implicits._
import progression_radar_yaml_generator.infrastructure.filesystem.exception.IncorrectFilenameException

class FileWriter[F[_]: Sync] {

  def writeFile(content: String, outputFile: String): F[File] =
    for {
      file       <- Sync[F].delay(new File(outputFile))
      _          <- checkIfIsNotADirectory(file)
      _          <- checkIfParentDirectoryExists(file)
      resultFile <- doWriteContentInFile(content, file)
    } yield resultFile

  private def doWriteContentInFile(content: String, file: File): F[File] = Sync[F].delay {
    if (!file.exists())
      file.createNewFile()

    val bw = new BufferedWriter(new JavaFileWriter(file))
    bw.write(content)
    bw.close
    file
  }

  private def checkIfIsNotADirectory(file: File): F[Unit] = Sync[F].delay {
    if (file.isDirectory)
      throw new IncorrectFilenameException(s"Incorrect filename: ${file.getAbsolutePath} is a directory")
  }

  private def checkIfParentDirectoryExists(file: File): F[Unit] = Sync[F].delay {
    if (!file.getParentFile.exists() || !file.getParentFile.canWrite)
      throw new IncorrectFilenameException(
        s"Incorrect filename: parent directory ${file.getParentFile} doesn't exists or there is a permissions issue with it"
      )
  }

}
