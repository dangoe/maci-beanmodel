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
package de.maci.beanmodel.generator.util

import java.lang.annotation.Annotation
import javax.lang.model.element._

import de.maci.beanmodel.Bean
import de.maci.beanmodel.generator.context.GenerationContext
import org.apache.commons.lang3.StringUtils

import scala.collection.JavaConversions._

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
object Elements {

  def isAnnotationPresent(element: Element, annotationTypes: Class[_ <: Annotation]*) = {

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

  def hasBeanValidSuperclass(typeElement: TypeElement, context: GenerationContext) = {

      def isSuperclassAnyRef =
        classOf[AnyRef].getCanonicalName == typeElement.getSuperclass.toString

      def existsValidSuperclassModel = {

          def resolveSuperClassTypeElement: (String) => Option[TypeElement] =
            n => Option(context.env.getElementUtils.getTypeElement(n))

        val superClassTypeElement = resolveSuperClassTypeElement(s"${typeElement.getSuperclass.toString}")

        superClassTypeElement.isDefined && isAnnotationPresent(superClassTypeElement.get, classOf[Bean])
      }

    !isSuperclassAnyRef && existsValidSuperclassModel
  }
}
