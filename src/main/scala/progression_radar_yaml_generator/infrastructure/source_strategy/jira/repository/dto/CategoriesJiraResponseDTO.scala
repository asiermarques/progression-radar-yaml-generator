package progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import progression_radar_yaml_generator.domain.{Category, KPI}

import scala.beans.BeanProperty
import scala.collection.mutable.HashMap

object CategoriesJiraResponseDTO {

  @JsonIgnoreProperties(ignoreUnknown = true)
  case class CategoriesJiraIssueStatusDTO(@BeanProperty name: String)

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(classOf[PropertyNamingStrategy.SnakeCaseStrategy])
  case class CategoriesJiraIssueDTO(@BeanProperty fields: CategoriesJiraIssueFieldsDTO)

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(classOf[PropertyNamingStrategy.SnakeCaseStrategy])
  case class CategoriesJiraIssueFieldsDTO(
    @BeanProperty summary: String,
    @BeanProperty description: String,
    @BeanProperty labels: Array[String],
    @BeanProperty status: CategoriesJiraIssueStatusDTO
  )

  @JsonIgnoreProperties(ignoreUnknown = true)
  case class CategoriesJiraResponseDTO(
    @BeanProperty issues: Array[CategoriesJiraIssueDTO]
  )
}
