package de.maci.beanmodel.generator.util

import javax.lang.model.element._

import de.maci.beanmodel.generator.testhelper.PackageElementMocker._
import de.maci.beanmodel.generator.testhelper.TypeElementMocker._
import de.maci.beanmodel.generator.testhelper.VariableElementMocker.mockVariableElement
import de.maci.beanmodel.generator.util.Elements._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.language.postfixOps

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 16.11.15
  */
@RunWith(classOf[JUnitRunner])
class ElementsVariableElementEvalAccessible$Test extends FlatSpec with Matchers with MockitoSugar {

  val packageElement = mockPackageElement withQualifiedName "de.maci" build

  "accessible" should "return true if VariableElement is public" in {
    val typeElement = typeElementEclosing(typeElement => Set(mockVariableElement
      .withEnclosingElement(() => typeElement)
      .withKind(ElementKind.FIELD)
      .withModifiers(Modifier.PUBLIC)
      .withSimpleName("getValue")
      .build))

    is(typeElement.getEnclosedElements.get(0).asInstanceOf[VariableElement]).accessible shouldBe true
  }

  it should "return true if corresponding public getters and setters exist" in {
    lazy val typeElement: TypeElement = mockTypeElement withKind ElementKind.CLASS withEnclosingElement (() => packageElement) withSimpleName "SomeName" withEnclosedElements(
      mockVariableElement withEnclosingElement (() => typeElement) withKind ElementKind.METHOD withSimpleName "getValue" withModifiers Modifier.PUBLIC build,
      mockVariableElement withEnclosingElement (() => typeElement) withKind ElementKind.METHOD withSimpleName "setValue" withModifiers Modifier.PUBLIC build,
      mockVariableElement withEnclosingElement (() => typeElement) withKind ElementKind.FIELD withSimpleName "value" withModifiers Modifier.PRIVATE build) build

    is(typeElement.getEnclosedElements.toList.find(_.getSimpleName.toString eq "value").get.asInstanceOf[VariableElement]).accessible shouldBe true
  }

  it should "return false if VariableElement is private" in {
    val elem = mockVariableElement.withModifiers(Modifier.PRIVATE).build
    is(elem).accessible shouldBe false
  }

  it should "return false if VariableElement is protected" in {
    val elem = mockVariableElement.withModifiers(Modifier.PROTECTED).build
    is(elem).accessible shouldBe false
  }

  private def typeElementEclosing(enclosedElementFactory: TypeElement => Set[Element]): TypeElement = {
    lazy val typeElement: TypeElement = mockTypeElement
      .withKind(ElementKind.CLASS)
      .withEnclosingElement(() => packageElement)
      .withSimpleName("SomeName")
      .withEnclosedElements(
        enclosedElementFactory.apply(typeElement)
      ).build
    return typeElement
  }
}
