package progression_radar_yaml_generator.infrastructure.formatter.yaml.dto

import progression_radar_yaml_generator.domain.Category

import scala.beans.BeanProperty

case class CategoryYamlDto(
  @BeanProperty key: String,
  @BeanProperty name: String,
  @BeanProperty description: String,
  @BeanProperty kpis: Array[KPIYamlDto]
)
object CategoryYamlDto {
  def fromDomain(category: Category): CategoryYamlDto = CategoryYamlDto(
    key = category.key,
    name = category.name,
    description = category.description,
    kpis = category.kpis.map(KPIYamlDto.fromDomain).toArray
  )
}
