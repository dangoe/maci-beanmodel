/**
 * Copyright (c) ${year} Daniel Götten
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

