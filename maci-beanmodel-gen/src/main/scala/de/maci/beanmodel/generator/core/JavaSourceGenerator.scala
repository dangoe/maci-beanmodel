/**
  * Copyright 2015 Daniel Götten
  * <p>
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * <p>
  * http://www.apache.org/licenses/LICENSE-2.0
  * <p>
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.maci.beanmodel.generator.core

import java.lang.annotation.Annotation
import javax.lang.model.element._
import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.{ Bean, IgnoredProperty }
import org.apache.commons.lang3.StringUtils
import scala.annotation.tailrec
import scala.collection.JavaConversions._
import org.jboss.forge.roaster.model.source.JavaSource
import org.jboss.forge.roaster.model.source.JavaClassSource
import org.jboss.forge.roaster.model.JavaClass

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 22.08.14
 */
trait JavaSourceGenerator[T <: JavaSource[T]] {

  def generate: JavaSource[JavaClassSource]
}

protected[core] object JavaSourceGenerator {

  def propertiesOf(typeElement: TypeElement): List[PropertyDescriptor] = typeElement.getEnclosedElements.toList
    .filter(e => (ElementKind.FIELD eq e.getKind) && !containsAnnotation(e, classOf[IgnoredProperty]) && isAccessible(e.asInstanceOf[VariableElement]))
    .map(v => PropertyDescriptor(v.asInstanceOf[VariableElement]))

  def containsAnnotation(element: Element, annotationTypes: Class[_ <: Annotation]*) = {

    def annotation: (AnnotationMirror) => Boolean = {
      def asCanonicalName: (Class[_ <: Annotation]) => String =
        (t: Class[_ <: Annotation]) => t.getCanonicalName

      a => annotationTypes.toSet.map(asCanonicalName)
        .contains(a.getAnnotationType.asElement().asInstanceOf[TypeElement].getQualifiedName.toString)
    }

    element.getAnnotationMirrors.toSet.exists(annotation)
  }

  def isAccessible(variableElement: VariableElement) = {
    val typeElement = variableElement.getEnclosingElement.asInstanceOf[TypeElement]
    val variableName = variableElement.getSimpleName.toString

    def existsMethod(prefix: String) = {
      def accessorMatching: (Element) => Boolean =
        s"$prefix${StringUtils.capitalize(variableName)}" == _.getSimpleName.toString

      typeElement.getEnclosedElements.toList.filter(_.getKind eq ElementKind.METHOD).exists(accessorMatching)
    }

    existsMethod("get") && existsMethod("set")
  }

  def hasBeanValidSuperclass(elem: TypeElement, context: GenerationContext) = {

    def isSuperclassAnyRef =
      classOf[AnyRef].getCanonicalName == elem.getSuperclass.toString

    def existsValidSuperclassModel = {

      def resolveSuperClassTypeElement: (String) => Option[TypeElement] =
        n => Option(context.env.getElementUtils.getTypeElement(n))

      val superClassTypeElement = resolveSuperClassTypeElement(s"${elem.getSuperclass.toString}")

      superClassTypeElement.isDefined && containsAnnotation(superClassTypeElement.get, classOf[Bean])
    }

    !isSuperclassAnyRef && existsValidSuperclassModel
  }

  def withNewline: (String) => String = text => s"${text}${System.lineSeparator}"

  def resolveTypesToImport(propertyModels: List[PropertyDescriptor], context: GenerationContext): Set[String] =
    resolveTypesToImport(propertyModels, Set(), context)

  @tailrec
  private[this] def resolveTypesToImport(propertyModels: List[PropertyDescriptor], typesToImport: Set[String], context: GenerationContext): Set[String] = {
    if (propertyModels.isEmpty) {
      return typesToImport
    }

    resolveTypesToImport(propertyModels.drop(1), propertyModels.head.usedTypes(context) ++ typesToImport, context)
  }
}
