package progression_radar_yaml_generator.domain

import progression_radar_yaml_generator.BaseUnitSpec

class CategorySpec extends BaseUnitSpec {

  describe("Category") {

    it("should generate category keys with four digits as max from the category name") {

      Category.createKeyForCategoryName("Communication") shouldBe "cmmn"
      Category.createKeyForCategoryName("Commit") shouldBe "cmmt"
      Category.createKeyForCategoryName("Infrastructure") shouldBe "infr"
      Category.createKeyForCategoryName("Engineering") shouldBe "engn"
      Category.createKeyForCategoryName("Kubernetes") shouldBe "kbrn"
    }

    it("should generate category keys with the first character of every world in a sentence") {

      Category.createKeyForCategoryName("Platform as a service") shouldBe "paas"
      Category.createKeyForCategoryName("Research & Development") shouldBe "rd"
    }

    it("should be tolerant to empty strings or short strings") {
      Category.createKeyForCategoryName("aeiuo") shouldBe "a"
      Category.createKeyForCategoryName("a") shouldBe "a"
      Category.createKeyForCategoryName("") shouldBe ""
    }

  }

}
