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

  @tailrec
  private def processCategoriesKPIs(
    issues: Array[CategoriesJiraIssueDTO],
    result: Seq[Category] = Seq.empty
  ): Seq[Category] =
    if (issues.isEmpty)
      result
    else {
      val issueDTO = issues.head
      val kpi = KPI(
        summary = issueDTO.fields.summary,
        description = issueDTO.fields.description,
        level = issueDTO.fields.labels
          .map(_.split("\\D+").filter(_.nonEmpty).map(_.toInt).headOption.getOrElse(0))
          .headOption
          .getOrElse(0),
        tags = Seq.empty
      )
      val categoryName = issueDTO.fields.status.name
      result.find(category => category.name == categoryName) match {
        case Some(category) => {
          processCategoriesKPIs(
            issues.tail,
            result.filter(_.name != categoryName) :+ category.copy(kpis = category.kpis :+ kpi)
          )
        }
        case None =>
          processCategoriesKPIs(
            issues.tail,
            result :+ Category(
              key = categoryName.substring(4).toLowerCase,
              name = categoryName,
              description = "",
              kpis = Seq(kpi)
            )
          )
      }
    }

  private def sortCategoriesByName(categories: Seq[Category]): Seq[Category] =
    categories.sortBy(_.name)

  private def sortCategoriesKPIsByLevel(categories: Seq[Category]): Seq[Category] =
    categories.map(category => category.copy(kpis = category.kpis.sortBy(_.level)))

}
