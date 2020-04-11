package progression_radar_yaml_generator.infrastructure.filesystem

import java.nio.file.{Files, Path}

import cats.effect.IO
import progression_radar_yaml_generator.BaseUnitSpec
import java.io.File
import java.nio.charset.StandardCharsets

import progression_radar_yaml_generator.infrastructure.filesystem.exception.IncorrectFilenameException

import scala.reflect.io.Directory

class FileWriterSpec extends BaseUnitSpec {

  val testFilePrefix             = "progression-radar-yaml-generator-file-writer"
  val fileWriter: FileWriter[IO] = new FileWriter[IO]

  val outputFile = File.createTempFile(testFilePrefix, ".yml")

  after {
    if (outputFile.exists())
      outputFile.delete()
  }

  describe("Writing the content in the specified file") {

    val simpleContent = """This is 
a simple
content"""
    val filename      = outputFile.getAbsolutePath

    def contentOfFile(file: File): String =
      Files.readAllLines(file.toPath, StandardCharsets.UTF_8).toArray.map(_.toString).reduce(_ + "\n" + _)

    describe("WHEN the file exists") {

      val writtenFile: File = fileWriter.writeFile(simpleContent, filename).unsafeRunSync()
      val content           = contentOfFile(writtenFile)

      it("should write the content in the file") {
        content shouldBe simpleContent
      }

      it("should replace the content in the file") {
        val newContent: String = "content2"
        fileWriter.writeFile(newContent, filename).unsafeRunSync()
        contentOfFile(writtenFile) shouldBe newContent
      }

    }

    describe("WHEN the file doesn't exists") {

      outputFile.delete
      val writtenFile: File = fileWriter.writeFile(simpleContent, filename).unsafeRunSync()
      val content           = contentOfFile(writtenFile)
      it("should create the file and write the content in it") {
        content shouldBe simpleContent
      }

      it("should throw an exception if the specified file is a directory") {
        intercept[IncorrectFilenameException] {
          fileWriter.writeFile(simpleContent, outputFile.getParentFile.getAbsolutePath).unsafeRunSync()
        }
      }
      it("should throw an exception if the parent directory of the specified file can not be writable") {
        intercept[IncorrectFilenameException] {
          fileWriter.writeFile(simpleContent, "/test.yaml").unsafeRunSync()
        }
      }
      it("should throw an exception if the parent directory of the specified file does not exist") {
        intercept[IncorrectFilenameException] {
          fileWriter.writeFile(simpleContent, "/_not_exists_/test.yaml").unsafeRunSync()
        }
      }
    }

  }

}
