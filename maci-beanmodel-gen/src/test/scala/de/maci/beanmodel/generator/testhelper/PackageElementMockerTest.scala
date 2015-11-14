package de.maci.beanmodel.generator.testhelper

import javax.lang.model.element.ElementKind
import de.maci.beanmodel.generator.testhelper.PackageElementMocker._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ FlatSpec, Matchers }
import javax.lang.model.element.PackageElement

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
@RunWith(classOf[JUnitRunner])
class PackageElementMockerTest extends FlatSpec with Matchers {

  "The mocker" should "should return a valid instance if the qualified name is set." in {
    val packageElement = mockPackageElement withQualifiedName ("de.maci") build

    defaultChecks(packageElement)

    packageElement.getQualifiedName.toString shouldBe "de.maci"
    packageElement.getSimpleName.toString shouldBe "maci"
    packageElement.isUnnamed() shouldBe false
  }

  it should "should return a valid default package instance if the name is empty." in {
    val packageElement = mockPackageElement withQualifiedName ("") build

    defaultChecks(packageElement)

    packageElement.getQualifiedName.toString shouldBe ""
    packageElement.getSimpleName.toString shouldBe ""
    packageElement.isUnnamed() shouldBe true
  }

  it should "should return a valid default package instance if the name is not set." in {
    val packageElement = mockPackageElement build

    defaultChecks(packageElement)

    packageElement.getQualifiedName.toString shouldBe ""
    packageElement.getSimpleName.toString shouldBe ""
    packageElement.isUnnamed() shouldBe true
  }

  private def defaultChecks(packageElement: PackageElement) = {
    packageElement.getKind shouldBe ElementKind.PACKAGE
    packageElement.getEnclosingElement shouldBe null
    packageElement.getEnclosedElements shouldBe empty
  }
}
