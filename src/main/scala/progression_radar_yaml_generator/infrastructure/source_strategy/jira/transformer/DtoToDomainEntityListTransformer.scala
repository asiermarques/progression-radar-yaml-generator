package progression_radar_yaml_generator.infrastructure.source_strategy.jira

import progression_radar_yaml_generator.domain.{Category, KPI}
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.dto.CategoriesJiraResponseDTO.{
  CategoriesJiraIssueDTO,
  CategoriesJiraResponseDTO
}
import scala.annotation.tailrec
import scala.util.chaining._
import scala.language.implicitConversions

object DtoToDomainEntityListTransformer {

  def transform(responseDTO: CategoriesJiraResponseDTO): Seq[Category] =
    processCategoriesKPIs(responseDTO.issues) pipe sortCategoriesByName pipe sortCategoriesKPIsByLevel

  def processCategoriesKPIs(issues: Array[CategoriesJiraIssueDTO]): Seq[Category] =
    issues
      .map(issue => (issue.fields.status.name, createKPI(issue)))
      .groupMap(_._1)(_._2)
      .map(kpiMap =>
        Category(
          name = kpiMap._1,
          key = kpiMap._1.substring(0, 4).toLowerCase,
          description = "",
          kpis = kpiMap._2.toSeq
        )
      )
      .toSeq

  private def createKPI(issueDTO: CategoriesJiraIssueDTO) = KPI(
    summary = issueDTO.fields.summary,
    description = issueDTO.fields.description,
    level = issueDTO.fields.labels
      .map(_.split("\\D+").filter(_.nonEmpty).map(_.toInt).headOption.getOrElse(0))
      .headOption
      .getOrElse(0),
    tags = Seq.empty
  )

  private def sortCategoriesByName(categories: Seq[Category]): Seq[Category] =
    categories.sortBy(_.name)

  private def sortCategoriesKPIsByLevel(categories: Seq[Category]): Seq[Category] =
    categories.map(category => category.copy(kpis = category.kpis.sortBy(_.level)))

}
