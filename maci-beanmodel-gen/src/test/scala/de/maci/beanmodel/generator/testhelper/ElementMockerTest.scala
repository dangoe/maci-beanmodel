package de.maci.beanmodel.generator.testhelper

import javax.lang.model.element.ElementKind
import de.maci.beanmodel.generator.testhelper.NameMocker._
import de.maci.beanmodel.generator.testhelper.PackageElementMocker._
import de.maci.beanmodel.generator.testhelper.ElementMocker._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ FlatSpec, Matchers }
import javax.lang.model.element.Element
import org.scalatest.mock.MockitoSugar
import javax.lang.model.element.Modifier
import org.mockito.exceptions.misusing.UnfinishedStubbingException

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
@RunWith(classOf[JUnitRunner])
class ElementMockerTest extends FlatSpec with MockitoSugar with Matchers {

  private val someElementKind = ElementKind.OTHER
  private val someElement = mock[Element]
  private val someModifier = Modifier.PUBLIC
  private val someOtherModifier = Modifier.FINAL

  "The mocker" should "should return a valid instance." in {
    val element = mockElement withKind (someElementKind) withEnclosingElement (() => someElement) withSimpleName ("SomeName") withModifiers (someModifier, someOtherModifier) build

    element.getKind shouldBe someElementKind
    element.getEnclosingElement shouldBe someElement
    element.getSimpleName.toString shouldBe "SomeName"
    element.getModifiers should contain theSameElementsAs Set(someModifier, someOtherModifier)
  }

  it should "throw an AssertionError on build if ElementKind is not set." in {
    intercept[AssertionError] {
      mockElement withEnclosingElement (() => someElement) withSimpleName ("SomeName") withModifiers (someModifier, someOtherModifier) build
    }
  }

  it should "throw an AssertionError on build if the enclosing element is not set." in {
    intercept[AssertionError] {
      mockElement withKind (someElementKind) withSimpleName ("SomeName") withModifiers (someModifier, someOtherModifier) build
    }
  }

  it should "throw an AssertionError on build if the simple name is not set." in {
    intercept[AssertionError] {
      mockElement withKind (someElementKind) withEnclosingElement (() => someElement) withModifiers (someModifier, someOtherModifier) build
    }
  }

  it should "throw an IllegalArgumentException if the simple name is empty." in {
    intercept[IllegalArgumentException] {
      mockElement withSimpleName ("")
    }
  }
}
