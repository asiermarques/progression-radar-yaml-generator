package progression_radar_yaml_generator.infrastructure.formatter.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import progression_radar_yaml_generator.BaseUnitSpec
import progression_radar_yaml_generator.domain.{Category, KPI}

class CategoriesYamlFormatterFunctionalSpec extends BaseUnitSpec {

  def formatter = new CategoriesYamlFormatter(new ObjectMapper(new YAMLFactory()))

  val fixture: Seq[Category] = Seq(
    Category(
      key = "test",
      name = "test",
      description = "test",
      kpis = Seq(
        KPI(summary = "test", description = "test", level = 1, tags = Array("test")),
        KPI(summary = "test", description = "test", level = 1, tags = Array("test"))
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
      formatter.format(Seq.empty[Category]) shouldBe ""
    }

  }

}
