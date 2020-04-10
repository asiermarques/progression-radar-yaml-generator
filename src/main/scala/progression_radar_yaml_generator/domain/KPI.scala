package progression_radar_yaml_generator.domain

case class KPI (name:String, description:String, level:Int, tags: Seq[String] = Seq.empty[String])