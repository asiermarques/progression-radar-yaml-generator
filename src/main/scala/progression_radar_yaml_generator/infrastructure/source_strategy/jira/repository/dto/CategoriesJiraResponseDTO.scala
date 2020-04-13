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

  def toCategories(responseDTO: CategoriesJiraResponseDTO): Seq[Category] = {

    var categoriesKPIs: HashMap[String, Seq[KPI]] = HashMap.empty[String, Seq[KPI]]
    responseDTO.issues.foreach { issueDTO =>
      val category: String = issueDTO.fields.status.name
      val kpi = KPI(
        summary = issueDTO.fields.summary,
        description = issueDTO.fields.description,
        level = issueDTO.fields.labels
          .map(_.split("\\D+").filter(_.nonEmpty).map(_.toInt).headOption.getOrElse(0))
          .headOption
          .getOrElse(0)
      )

      if (categoriesKPIs.keys.toArray.exists(_.equals(category)))
        categoriesKPIs(category) = categoriesKPIs(category) :+ kpi
      else
        categoriesKPIs += (category -> Seq(kpi))
    }

    categoriesKPIs.keys
      .map(name => Category(key = name.substring(0, 4), name = name, description = "", kpis = categoriesKPIs(name)))
      .toSeq

  }
}
