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

import javax.lang.model.`type`.TypeMirror

import scala.util.matching.Regex.Match

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 21.08.14
 */
object TypeMirrors {

  // TODO Simplify!

  def simpleName(t: TypeMirror) = """^(\w[a-zA-Z0-9_]*\.)*(\w[a-zA-Z0-9\$]*)?$""".r
    .findFirstMatchIn(canonicalName(t)).get.group(2)

  def canonicalName(t: TypeMirror) = """^((\w[a-zA-Z0-9_]*\.)*\w[a-zA-Z0-9\$]*)(<.*)?$""".r
    .findFirstMatchIn(t.toString).get.group(1)

  def simplify(typeMirror: TypeMirror) = """((\w[a-z0-9_\-]*\.)+(\w[a-zA-Z0-9\$]*))""".r
    .replaceAllIn(typeMirror.toString, (m: Match) => m.group(m.groupCount))

  def relvantTypes(typeMirror: TypeMirror) = """((\w[a-z0-9_\-]*\.)+(\w[a-zA-Z0-9\$]*))""".r
    .findAllMatchIn(typeMirror.toString).map(m => m.toString()).toSet
}
