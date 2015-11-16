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

import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element._

import de.maci.beanmodel.Bean
import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.util.Elements._

import scala.collection.JavaConversions._

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 22.08.14
  */
abstract class TypeElementProcessor[R](context: GenerationContext, typeElement: TypeElement) {

  def process: R

  protected def elementUtils = context.env.getElementUtils

  protected def variableElements = enclosedElements.filter(e => FIELD eq e.getKind).map(v => v.asInstanceOf[VariableElement])

  protected def enclosedElements = typeElement.getEnclosedElements.toList

  protected def superclass = Option(elementUtils.getTypeElement(typeElement.getSuperclass.toString))

  protected def superclassIsRelevantBean = superclass.exists(s => is(s).annotatedWithAllOf(classOf[Bean]))
}
