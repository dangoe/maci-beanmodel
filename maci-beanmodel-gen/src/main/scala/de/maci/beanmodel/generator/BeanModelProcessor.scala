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
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.{Element, TypeElement}

import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.core.BeanPropertyModelSourceGenerator
import de.maci.beanmodel.generator.core.io.JavaSourceWriter
import de.maci.beanmodel.generator.util.Elements._

import scala.collection.JavaConversions._

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 18.08.14
  */
@SupportedAnnotationTypes(Array("de.maci.beanmodel.Bean", "javax.persistence.Entity"))
final class BeanModelProcessor extends AbstractProcessor {

  def process(annotations: java.util.Set[_ <: TypeElement], roundEnv: RoundEnvironment): Boolean = {
    val context = GenerationContext(processingEnv)
    val sourceWriter = JavaSourceWriter.forContext(context)

    val sourceGen = BeanPropertyModelSourceGenerator(context) _

    typeElementsToBeProcessed(annotations.toSet, roundEnv).foreach(e => sourceWriter.write(sourceGen(e).process))

    return true
  }

  private[this] def typeElementsToBeProcessed(annotations: Set[_ <: TypeElement], roundEnv: RoundEnvironment): Set[TypeElement] = {
    def annotatedWith: (TypeElement) => Set[Element] = roundEnv.getElementsAnnotatedWith(_).toSet

    return annotations.flatMap(annotatedWith).filter(is(_).ofKind(CLASS)).map(_.asInstanceOf[TypeElement])
  }
}
