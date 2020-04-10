package progression_radar_yaml_generator.infrastructure.strategy

import progression_radar_yaml_generator.domain.CategoryRepository

import scala.collection.immutable.Map

class SourceContext[F[_]] {

  private var currentSource: SourceImplementation.Value = _

  private var categoryRepositories: Map[SourceImplementation.Value, CategoryRepository[F]] =
    Map.empty[SourceImplementation.Value, CategoryRepository[F]]

  def setSourceContext(source: SourceImplementation.Value): Unit =
    currentSource = source

  def registerCategoryRepository(source: SourceImplementation.Value, repository: CategoryRepository[F]): Unit =
    categoryRepositories += (source -> repository)

  def getCategoryRepository: CategoryRepository[F] = categoryRepositories(currentSource)

}
