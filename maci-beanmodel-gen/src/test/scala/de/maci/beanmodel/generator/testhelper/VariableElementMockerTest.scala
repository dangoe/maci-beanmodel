package de.maci.beanmodel.generator.testhelper

import scala.collection.JavaConverters.asScalaBufferConverter

import org.junit.runner.RunWith
import org.scalatest.Finders
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import de.maci.beanmodel.generator.testhelper.VariableElementMocker.mockVariableElement
import de.maci.beanmodel.generator.testhelper.ElementMocker.mockElement
import de.maci.beanmodel.generator.testhelper.PackageElementMocker.mockPackageElement
import de.maci.beanmodel.generator.testhelper.TypeElementMocker.mockTypeElement
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
@RunWith(classOf[JUnitRunner])
class VariableElementMockerTest extends FlatSpec with Matchers {

  val typeElement = mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => mockPackageElement withQualifiedName ("de.maci") build) withSimpleName ("SomeName") build

  "The mocker" should "should return a valid instance if all required attributes are set." in {
    val variableElement = mockVariableElement withKind (ElementKind.FIELD) withEnclosingElement (() => typeElement) withSimpleName ("someVariable") withModifiers (Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL) build

    variableElement.getKind shouldBe ElementKind.FIELD
    variableElement.getEnclosingElement shouldBe typeElement
    variableElement.getSimpleName.toString shouldBe "someVariable"
    variableElement.getModifiers should contain theSameElementsAs Set(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
  }

  it should "throw an AssertionError on build if ElementKind is not set." in {
    intercept[AssertionError] {
      mockVariableElement withEnclosingElement (() => typeElement) withSimpleName ("SomeName") build
    }
  }

  it should "throw an AssertionError on build if the enclosing element is not set." in {
    intercept[AssertionError] {
      mockVariableElement withKind (ElementKind.FIELD) withSimpleName ("SomeName") build
    }
  }

  it should "throw an AssertionError on build if the simple name is not set." in {
    intercept[AssertionError] {
      mockVariableElement withKind (ElementKind.FIELD) withEnclosingElement (() => typeElement) build
    }
  }

  it should "throw an IllegalArgumentException if the simple name is empty." in {
    intercept[IllegalArgumentException] {
      mockVariableElement withSimpleName ("")
    }
  }
}
