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

import javax.lang.model.element.VariableElement

import de.maci.beanmodel.Property
import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.util.TypeMirrors.{canonicalName, simpleName}

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 18.08.14
 */
protected[core] class PropertyDescriptor private (val element: VariableElement) {

  require(Option(element).isDefined, s"VariableElement must not be null")

  val declaringClass = simpleName(element.getEnclosingElement.asType)
  val name = element.getSimpleName
  val `type` = simpleName(element.asType)

  def usedTypes(context: GenerationContext): Set[String] =
    // TODO do not import if same package
    Set(classOf[Property[_]].getCanonicalName, element.getEnclosingElement.asType.toString, canonicalName(element.asType))
}

protected[core] object PropertyDescriptor {

  def apply(element: VariableElement) = new PropertyDescriptor(element)
}
