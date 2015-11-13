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
package de.maci.beanmodel.generator

import javax.annotation.processing.{AbstractProcessor, RoundEnvironment, SupportedAnnotationTypes}
import javax.lang.model.element.{Element, ElementKind, TypeElement}

import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.source.model.BeanPropertyModelSourceModel
import de.maci.beanmodel.generator.util.ClassWriter.write

import scala.collection.JavaConversions._

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 18.08.14
 */
@SupportedAnnotationTypes(Array("de.maci.beanmodel.Bean", "javax.persistence.Entity"))
final class BeanModelProcessor extends AbstractProcessor {

  def process(annotations: java.util.Set[_ <: TypeElement], roundEnv: RoundEnvironment): Boolean = {
    val context = GenerationContext(processingEnv)

    typeElementsToBeProcessed(annotations.toSet, roundEnv).toList.sortWith(sortOrder)
      .foreach(e => write(Set(BeanPropertyModelSourceModel(e, context)), context))

    true
  }

  private[this] def sortOrder: (TypeElement, TypeElement) => Boolean =
    (t, o) => true // TODO To be implemented ...

  private[this] def typeElementsToBeProcessed(annotations: Set[_ <: TypeElement], roundEnv: RoundEnvironment) = {
    def annotatedWith: (TypeElement) => Set[Element] = roundEnv.getElementsAnnotatedWith(_).toSet
    def isTypeElement: (Element) => Boolean = _.getKind == ElementKind.CLASS

    annotations.flatMap(annotatedWith).filter(isTypeElement).map(_.asInstanceOf[TypeElement])
  }
}

