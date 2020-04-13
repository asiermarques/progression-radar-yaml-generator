package progression_radar_yaml_generator.infrastructure.source_strategy.jira.transformer

import progression_radar_yaml_generator.BaseUnitSpec
import progression_radar_yaml_generator.domain.{Category, KPI}
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.DtoToDomainEntityListTransformer
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.dto.CategoriesJiraResponseDTO._

class DtoToDomainEntityListTransformerSpec extends BaseUnitSpec {

  val fixture: CategoriesJiraResponseDTO =
    CategoriesJiraResponseDTO(
      issues = Array(
        CategoriesJiraIssueDTO(
          fields = CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "category1"),
            summary = "test",
            description = "test",
            labels = Array("L1")
          )
        ),
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "category2"),
            summary = "test2",
            description = "test2",
            labels = Array("L2")
          )
        ),
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "category2"),
            summary = "test3",
            description = "test3",
            labels = Array("L2")
          )
        )
      )
    )

  describe("Jira Response DTO to Domain Entity transformer") {

    it("should be transformed to a list of domain categories") {
      val result: Seq[Category] = DtoToDomainEntityListTransformer.transform(fixture)
      result.size shouldBe 2

      val category2 = result.findLast(_.name.equals("category2")).get
      category2.kpis.length shouldBe 2
      category2.kpis.isInstanceOf[Seq[KPI]] shouldBe true
      category2.name shouldBe "category2"
      category2.description shouldBe ""

      val firstKPI = category2.kpis.head
      firstKPI.summary shouldBe "test2"
      firstKPI.description shouldBe "test2"
      firstKPI.level shouldBe 2
    }

    it("should transform a dto with empty result") {
      val result: Seq[Category] =
        DtoToDomainEntityListTransformer.transform(CategoriesJiraResponseDTO(issues = Array.empty))
      result.isEmpty shouldBe true
    }

  }

}
