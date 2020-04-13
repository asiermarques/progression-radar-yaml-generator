package progression_radar_yaml_generator.fixture

import progression_radar_yaml_generator.infrastructure.source_strategy.jira.repository.dto.CategoriesJiraResponseDTO._

object CategoriesJiraResponseDTOGenerator {

  def generate(totalIssues: Int): CategoriesJiraResponseDTO =
    CategoriesJiraResponseDTO(issues =
      (1 to totalIssues)
        .map(number =>
          CategoriesJiraIssueDTO(fields =
            CategoriesJiraIssueFieldsDTO(
              summary = "test" + number.toString,
              description = "test",
              labels = Array.empty[String],
              status = CategoriesJiraIssueStatusDTO(name = "test" + (if (number % 2 == 0) "_2"))
            )
          )
        )
        .toArray
    )

}
