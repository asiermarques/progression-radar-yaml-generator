package progression_radar_yaml_generator.infrastructure.formatter.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import progression_radar_yaml_generator.domain.Category
import progression_radar_yaml_generator.infrastructure.formatter.yaml.dto.CategoryYamlDto

class CategoriesYamlFormatter(mapper: ObjectMapper) {
  def format(categories: Seq[Category]): String =
    mapper
      .writerWithDefaultPrettyPrinter()
      .writeValueAsString(categories.map(CategoryYamlDto.fromDomain).toArray)
      .trim
}
