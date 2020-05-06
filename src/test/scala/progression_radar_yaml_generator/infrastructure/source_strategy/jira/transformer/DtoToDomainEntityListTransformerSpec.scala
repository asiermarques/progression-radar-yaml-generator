package progression_radar_yaml_generator.infrastructure.source_strategy.jira.transformer

import progression_radar_yaml_generator.BaseUnitSpec
import progression_radar_yaml_generator.domain.{Category, KPI}
import progression_radar_yaml_generator.infrastructure.source_strategy.jira.transformer.DtoToDomainEntityListTransformer
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
            labels = Array("L3")
          )
        )
      )
    )

  val unSortedKPIsFixture: CategoriesJiraResponseDTO =
    CategoriesJiraResponseDTO(
      issues = Array(
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "category1"),
            summary = "test2",
            description = "test2",
            labels = Array("L2")
          )
        ),
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "category1"),
            summary = "test1",
            description = "test1",
            labels = Array("L1")
          )
        )
      )
    )

  val unSortedCategoriesFixture: CategoriesJiraResponseDTO =
    CategoriesJiraResponseDTO(
      issues = Array(
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "Other category"),
            summary = "test2",
            description = "test2",
            labels = Array("L2")
          )
        ),
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "The category"),
            summary = "test2",
            description = "test2",
            labels = Array("L2")
          )
        ),
        CategoriesJiraIssueDTO(fields =
          CategoriesJiraIssueFieldsDTO(
            status = CategoriesJiraIssueStatusDTO(name = "A category"),
            summary = "test2",
            description = "test2",
            labels = Array("L2")
          )
        )
      )
    )

  describe("Jira Response DTO to Domain Entity transformer") {

    it("should transform the dto to a list of domain categories") {
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

    it("should transform the dto to a list of domain categories and order the KPIs by level") {
      val result: Seq[Category] = DtoToDomainEntityListTransformer.transform(unSortedKPIsFixture)
      result.size shouldBe 1

      val firstKPI = result.head.kpis.head
      firstKPI.summary shouldBe "test1"
      firstKPI.description shouldBe "test1"
      firstKPI.level shouldBe 1
    }

    it("should transform the dto to a list of domain categories and order the categories by name") {
      val result: Seq[Category] = DtoToDomainEntityListTransformer.transform(unSortedCategoriesFixture)
      result.size shouldBe 3

      result(0).name shouldBe "A category"
      result(1).name shouldBe "Other category"
      result(2).name shouldBe "The category"
    }

    it("should transform a dto with empty result") {
      val result: Seq[Category] =
        DtoToDomainEntityListTransformer.transform(CategoriesJiraResponseDTO(issues = Array.empty))
      result.isEmpty shouldBe true
    }

  }

}
