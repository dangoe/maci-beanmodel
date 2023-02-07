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

import org.apache.commons.lang3.StringUtils

import scala.collection.JavaConversions._

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 30.04.15
  */
object Elements {

  class ElementEval private[util](elem: Element) {

    def ofKind(kind: ElementKind) = elem.getKind eq kind

    def annotatedWithAllOf(annotationTypes: Class[_ <: Annotation]*): Boolean = {
      def annotation: (AnnotationMirror) => Boolean = {
        def asCanonicalName: (Class[_ <: Annotation]) => String =
          (t: Class[_ <: Annotation]) => t.getCanonicalName

        a => annotationTypes.toSet.map(asCanonicalName)
          .contains(a.getAnnotationType.asElement().asInstanceOf[TypeElement].getQualifiedName.toString)
      }

      elem.getAnnotationMirrors.toSet.exists(annotation)
    }
  }

  class VariableElementEval private[util](elem: VariableElement) extends ElementEval(elem) {

    def accessible: Boolean = {
      if (elem.getModifiers.contains(Modifier.PUBLIC)) {
        return true
      }

      val typeElement = elem.getEnclosingElement.asInstanceOf[TypeElement]
      val variableName = elem.getSimpleName.toString

      def existsMethod(prefix: String) = {
        def accessorMatching: (Element) => Boolean =
          s"$prefix${StringUtils.capitalize(variableName)}" == _.getSimpleName.toString

        typeElement.getEnclosedElements.toList.filter(_.getKind eq ElementKind.METHOD).exists(accessorMatching)
      }

      return existsMethod("get") && existsMethod("set")
    }
  }

  def is(elem: Element) = new ElementEval(elem)

  def is(elem: VariableElement) = new VariableElementEval(elem)
}
