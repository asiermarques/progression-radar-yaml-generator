package progression_radar_yaml_generator

import org.mockito.{ArgumentMatchersSugar, MockitoSugar}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

trait BaseUnitSpec
    extends AnyFunSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with Matchers
    with MockitoSugar
    with ArgumentMatchersSugar
