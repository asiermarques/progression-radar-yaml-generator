package progression_radar_yaml_generator.domain

case class KPI(summary: String, description: String, level: Int, tags: Seq[String] = Seq.empty[String])
