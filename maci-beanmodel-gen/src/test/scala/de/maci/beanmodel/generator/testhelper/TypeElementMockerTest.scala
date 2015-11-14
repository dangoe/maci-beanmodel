package de.maci.beanmodel.generator.testhelper

import scala.collection.JavaConverters.asScalaBufferConverter

import org.junit.runner.RunWith
import org.scalatest.Finders
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

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
class TypeElementMockerTest extends FlatSpec with Matchers {

  val packageElement = mockPackageElement withQualifiedName ("de.maci") build

  "The mocker" should "should return a valid class instance if enclosing element and simple name are set." in {
    val typeElement = mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => packageElement) withSimpleName ("SomeName") build

    typeElement.getKind shouldBe ElementKind.CLASS
    typeElement.getEnclosingElement shouldBe packageElement
    typeElement.getSimpleName.toString shouldBe "SomeName"
    typeElement.getQualifiedName.toString shouldBe "de.maci.SomeName"
  }

  it should "should return a valid interface instance if enclosing element and simple name are set." in {
    val typeElement = mockTypeElement withKind (ElementKind.INTERFACE) withEnclosingElement (() => packageElement) withSimpleName ("SomeName") build

    typeElement.getKind shouldBe ElementKind.INTERFACE
    typeElement.getEnclosingElement shouldBe packageElement
    typeElement.getSimpleName.toString shouldBe "SomeName"
    typeElement.getQualifiedName.toString shouldBe "de.maci.SomeName"
  }

  it should "should be possible to add enclosed elements." in {

    lazy val typeElement: TypeElement = mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => packageElement) withSimpleName ("SomeName") withEnclosedElements (
      mockElement withKind (ElementKind.METHOD) withEnclosingElement (() => typeElement) withSimpleName ("someMethod") withModifiers (Modifier.PUBLIC) build,
      mockElement withKind (ElementKind.METHOD) withEnclosingElement (() => typeElement) withSimpleName ("anotherMethod") withModifiers (Modifier.PUBLIC) build) build

    typeElement.getEnclosedElements.size() shouldBe 2

      def enclosedElementByName(e: TypeElement)(n: String) = e.getEnclosedElements.asScala.find(_.getSimpleName().toString eq n)

    val enclosedElementByNamePartly = enclosedElementByName(typeElement)_

    enclosedElementByNamePartly("someMethod").get.getEnclosingElement shouldBe typeElement
    enclosedElementByNamePartly("anotherMethod").get.getEnclosingElement shouldBe typeElement
  }

  it should "throw an AssertionError on build if ElementKind is not set." in {
    intercept[AssertionError] {
      mockTypeElement withEnclosingElement (() => packageElement) withSimpleName ("SomeName") build
    }
  }

  it should "throw an AssertionError on build if the enclosing element is not set." in {
    intercept[AssertionError] {
      mockTypeElement withKind (ElementKind.CLASS) withSimpleName ("SomeName") build
    }
  }

  it should "throw an AssertionError on build if the simple name is not set." in {
    intercept[AssertionError] {
      mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => packageElement) build
    }
  }

  it should "throw an IllegalArgumentException if the simple name is empty." in {
    intercept[IllegalArgumentException] {
      mockTypeElement withSimpleName ("")
    }
  }
}
