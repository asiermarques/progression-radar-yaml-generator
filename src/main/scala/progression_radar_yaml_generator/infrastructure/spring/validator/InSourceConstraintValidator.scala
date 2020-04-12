package progression_radar_yaml_generator.infrastructure.spring.validator

import javax.validation.{ConstraintValidator, ConstraintValidatorContext}
import progression_radar_yaml_generator.infrastructure.strategy.SourceImplementation
import progression_radar_yaml_generator.infrastructure.spring.validator.annotation.InSource

class InSourceConstraintValidator extends ConstraintValidator[InSource, String] {

  private val valueList: Seq[String] = SourceImplementation.values.toArray.map(_.toString.toUpperCase)

  override def initialize(constraintAnnotation: InSource): Unit = ()

  override def isValid(value: String, context: ConstraintValidatorContext): Boolean =
    valueList.contains(value.toUpperCase)
}
