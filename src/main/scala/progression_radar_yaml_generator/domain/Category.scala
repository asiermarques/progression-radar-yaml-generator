package progression_radar_yaml_generator.domain

case class Category(key: String, name: String, description: String, kpis: Seq[KPI])
object Category {
  def createKeyForCategoryName(categoryName: String): String = {
    val max                    = 4
    val initialCleaned: String = categoryName.map(_.toLower).filter(_.toString.matches("[a-z0-9 ]+")).trim
    def processTail(tail: String): String =
      tail
        .filter(_.toString.matches("[^aeiou]"))
        .toList
        .take(max - 1)
        .mkString("")

    if (initialCleaned.contains(" "))
      initialCleaned.split(" ").filter(!_.trim.isEmpty).map(_.head.toString).mkString("")
    else
      initialCleaned.headOption.getOrElse("") + "" + processTail(initialCleaned.tail)
  }

}
