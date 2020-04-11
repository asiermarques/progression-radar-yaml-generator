package progression_radar_yaml_generator.infrastructure.filesystem.exception

class IncorrectFilenameException(message: String, nestedException: Throwable = null) extends Exception {
  override def getMessage: String  = message
  override def getCause: Throwable = nestedException
}
