package progression_radar_yaml_generator.infrastructure.formatter.yaml

import org.springframework.beans.factory.annotation.Autowired
import progression_radar_yaml_generator.{BaseIntegrationTest, BaseUnitSpec}
import progression_radar_yaml_generator.domain.{Category, KPI}
import progression_radar_yaml_generator.infrastructure.dto.{CategoryYamlDto, KPIYamlDto}

class CategoriesYamlFormatterIntegrationSpec extends BaseIntegrationTest {

  @Autowired
  var formatter: CategoriesYamlFormatter = _

  val fixture: Array[CategoryYamlDto] = Array(
    CategoryYamlDto(
      key = "test",
      name = "test",
      description = "test",
      kpis = Array(
        KPIYamlDto(summary = "test", description = "test", level = 1, tags = Array("test")),
        KPIYamlDto(summary = "test", description = "test", level = 1, tags = Array("test"))
      )
    )
  )

  val expectedFixture = """---
- key: "test"
  name: "test"
  description: "test"
  kpis:
  - summary: "test"
    description: "test"
    level: 1
    tags:
    - "test"
  - summary: "test"
    description: "test"
    level: 1
    tags:
    - "test"
"""

  describe("Export the category list to yaml") {

    it("should convert a specific list") {
      formatter.format(fixture) shouldBe expectedFixture
    }

    it("should convert an empty list") {
      formatter.format(Array.empty[CategoryYamlDto]) shouldBe ""
    }

  }

}
