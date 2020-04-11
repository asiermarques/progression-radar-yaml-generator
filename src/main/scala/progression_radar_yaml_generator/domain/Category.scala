package progression_radar_yaml_generator.domain

case class Category(key: String, name: String, description: String, kpis: Seq[KPI])
