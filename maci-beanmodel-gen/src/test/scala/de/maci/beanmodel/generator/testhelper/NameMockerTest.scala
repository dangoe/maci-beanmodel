package de.maci.beanmodel.generator.testhelper

import de.maci.beanmodel.generator.testhelper.NameMocker._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ FlatSpec, Matchers }

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
@RunWith(classOf[JUnitRunner])
class NameMockerTest extends FlatSpec with Matchers {

  "The mocker" should "should return a valid instance if the name is set." in {
    val name = mockName withContent ("de.maci") build

    name.toString shouldBe "de.maci"
  }

  it should "should return a valid instance if the name is empty." in {
    val name = mockName withContent ("") build

    name.toString shouldBe ""
  }

  it should "should return a valid instance if the name is not set." in {
    val name = mockName build

    name.toString shouldBe ""
  }
}
