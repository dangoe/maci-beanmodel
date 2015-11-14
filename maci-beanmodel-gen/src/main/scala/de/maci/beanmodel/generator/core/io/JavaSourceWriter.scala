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
package de.maci.beanmodel.generator.core.io

import java.io._
import javax.tools.Diagnostic.Kind
import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.util.Closeables.autoclose
import de.maci.beanmodel.generator.util.Exceptions.stackTraceOf
import org.jboss.forge.roaster.model.source.JavaSource

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 18.08.14
 */
object JavaSourceWriter {

  def write(source: JavaSource[_], context: GenerationContext) {

    def handleException: (Exception) => Unit =
      (e: Exception) => context.env.getMessager.printMessage(Kind.ERROR, stackTraceOf(e).getOrElse("Unknown cause"))

    val sourceFile = context.env.getFiler.createSourceFile(source.getCanonicalName)

    autoclose(() => sourceFile.openOutputStream, (os: OutputStream) => {
      autoclose(() => new PrintWriter(os), (w: PrintWriter) => {
        w.append(source.toString)
        w.flush()
        w.close()
      }, handleException)
    }, handleException)
  }
}
