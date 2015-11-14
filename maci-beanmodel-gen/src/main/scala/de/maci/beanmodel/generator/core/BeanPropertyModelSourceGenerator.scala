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

import java.time.LocalDate
import java.time.format.DateTimeFormatter._
import javax.annotation.Generated
import javax.lang.model.element.TypeElement

import de.maci.beanmodel.{BeanPropertyModel, Property}
import de.maci.beanmodel.generator.BeanModelProcessor
import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.core.JavaSourceGenerator.{hasBeanValidSuperclass, propertiesOf}
import de.maci.beanmodel.generator.util.TypeMirrors._
import org.jboss.forge.roaster.Roaster
import org.jboss.forge.roaster.model.source.{JavaClassSource, JavaSource}

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 22.08.14
 */
final class BeanPropertyModelSourceGenerator private (typeElement: TypeElement, context: GenerationContext) extends JavaSourceGenerator[JavaClassSource] {

  override def generate: JavaSource[JavaClassSource] = {
    val simpleTypeName = typeElement.getSimpleName.toString
    val canonicalTypeName = typeElement.getQualifiedName.toString

    val source = Roaster.create(classOf[JavaClassSource])
    source.setName(s"${simpleTypeName}Properties")
    source.setPackage(context.env.getElementUtils.getPackageOf(typeElement).getQualifiedName.toString)
      .setName(s"${simpleTypeName}Properties")
      .setPublic()

    if (hasBeanValidSuperclass(typeElement, context)) {
      source.setSuperType(s"${simpleName(typeElement.getSuperclass)}Properties")

      // TODO do not import if same package
      source.addImport(s"${canonicalName(typeElement.getSuperclass)}Properties")
    }

    if (context.isSuppressAllWarnings) {
      source.addAnnotation(classOf[SuppressWarnings]).setStringValue("all")
    }

    source.addAnnotation(classOf[BeanPropertyModel]).setStringValue(canonicalTypeName)

    source.addAnnotation(classOf[Generated])
      .setStringArrayValue(List(classOf[BeanModelProcessor].getCanonicalName).toArray)
      .setStringValue("date", ISO_LOCAL_DATE.format(LocalDate.now))

    if (propertiesOf(typeElement).nonEmpty) {
      source.addImport(classOf[Property[AnyRef]])
    }

    propertiesOf(typeElement).foreach(p => {
      source.addField(
        s"""public static final Property<${simplify(p.element.asType)}> ${p.name} =
           |new Property<>(${simpleName(p.element.getEnclosingElement.asType)}.class, "${p.name}", (Class<${simplify(p.element.asType)}>) (Class<?>) ${simpleName(p.element.asType)}.class);""".stripMargin)

      relvantTypes(p.element.asType).foreach(s => source.addImport(s))
    })

    return source
  }
}

object BeanPropertyModelSourceGenerator {

  def apply(typeElement: TypeElement, context: GenerationContext) = new BeanPropertyModelSourceGenerator(typeElement, context)

}