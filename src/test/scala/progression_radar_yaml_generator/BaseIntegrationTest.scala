package progression_radar_yaml_generator

import org.scalatest.BeforeAndAfterAll
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.jline.{InteractiveShellApplicationRunner, ScriptShellApplicationRunner}
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.support.DirtiesContextTestExecutionListener

@SpringBootTest(
  classes = Array(classOf[ProgressionRadarYamlGeneratorTestApp]),
  properties = Array(
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
  )
)
class BaseIntegrationTest extends BaseUnitSpec with BeforeAndAfterAll {

  private val testContextManager = new TestContextManager(this.getClass)

  override def beforeAll(): Unit = {
    testContextManager.registerTestExecutionListeners(SpringBootDependencyInjectionTestExecutionListener)
    testContextManager.beforeTestClass()
    testContextManager.prepareTestInstance(this)
  }

  override def afterAll(): Unit =
    testContextManager.afterTestClass()

  object DirtiesContextTestExecutionListener extends DirtiesContextTestExecutionListener

  object SpringBootDependencyInjectionTestExecutionListener extends SpringBootDependencyInjectionTestExecutionListener

}
