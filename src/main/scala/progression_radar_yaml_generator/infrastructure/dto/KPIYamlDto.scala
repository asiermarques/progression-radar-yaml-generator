package progression_radar_yaml_generator.infrastructure.dto

import progression_radar_yaml_generator.domain.KPI

import scala.beans.BeanProperty

case class KPIYamlDto(
  @BeanProperty summary: String,
  @BeanProperty description: String,
  @BeanProperty level: Int,
  @BeanProperty tags: Array[String]
)
object KPIYamlDto {
  def fromDomain(kpi: KPI): KPIYamlDto = KPIYamlDto(
    summary = kpi.summary,
    level = kpi.level,
    description = kpi.description,
    tags = kpi.tags.toArray
  )
}
