package progression_radar_yaml_generator.domain

trait CategoryRepository[F[_]] {
  def findAllCategories():F[Seq[Category]]
}
