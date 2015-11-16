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
package de.maci.beanmodel.generator.context

import java.nio.charset.Charset
import javax.annotation.processing.ProcessingEnvironment

import de.maci.beanmodel.generator.context.GenerationContext._

import scala.util.Try

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 18.08.14
  */
class GenerationContext private(val env: ProcessingEnvironment) {

  def encoding = Try(Charset.forName(option(PropertyEncoding).getOrElse(DefaultEncoding.name))).getOrElse(DefaultEncoding)

  def suppressAllWarnings = option(PropertySuppressAllWarnings).map(s => s.toBoolean).getOrElse(true)

  private def option(option: String) = Option(env.getOptions.get(option))
}

object GenerationContext {

  private[context] val DefaultEncoding = Charset.forName("UTF-8")

  private[context] val PropertyEncoding = "encoding"
  private[context] val PropertySuppressAllWarnings = "suppressAllWarnings"

  def apply(env: ProcessingEnvironment) = new GenerationContext(env)
}
