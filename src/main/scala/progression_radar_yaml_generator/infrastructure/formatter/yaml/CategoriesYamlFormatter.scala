package progression_radar_yaml_generator.infrastructure.formatter.yaml

import com.fasterxml.jackson.databind.ObjectMapper
import progression_radar_yaml_generator.infrastructure.dto.CategoryYamlDto

class CategoriesYamlFormatter(mapper: ObjectMapper = null) {
  def format(categories: Array[CategoryYamlDto]): String =
    if (categories.isEmpty) ""
    else mapper.writerWithDefaultPrettyPrinter().writeValueAsString(categories)
}
