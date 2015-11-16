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
import java.time.format.DateTimeFormatter
import javax.annotation.Generated
import javax.lang.model.element.{TypeElement, VariableElement}

import de.maci.beanmodel.generator.BeanModelProcessor
import de.maci.beanmodel.generator.context.GenerationContext
import de.maci.beanmodel.generator.util.Elements._
import de.maci.beanmodel.generator.util.TypeMirrors._
import de.maci.beanmodel.{BeanPropertyModel, IgnoredProperty, Property}
import org.jboss.forge.roaster.Roaster
import org.jboss.forge.roaster.model.source.{JavaClassSource, JavaSource}

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 22.08.14
  */
final class BeanPropertyModelSourceGenerator private(context: GenerationContext, typeElement: TypeElement)
  extends TypeElementProcessor[JavaSource[JavaClassSource]](context, typeElement) {

  private val notIgnoredAndAccessible: (VariableElement) => Boolean =
    v => !is(v).annotatedWithAllOf(classOf[IgnoredProperty]) && is(v).accessible

  override def process: JavaSource[JavaClassSource] = {
    val source = createBaseClassSource(typeElement, context)
    val relevantVariableElements = variableElements.filter(notIgnoredAndAccessible)

    if (relevantVariableElements.nonEmpty)
      source.addImport(classOf[Property[AnyRef]])

    relevantVariableElements.foreach(v => {
      val name = v.getSimpleName
      source.addField(
        s"""public static final Property<${simplify(v.asType)}> $name =
            |new Property<>(${simpleName(v.getEnclosingElement.asType)}.class, "$name",
            |(Class<${simplify(v.asType)}>) (Class<?>) ${simpleName(v.asType)}.class);""".stripMargin)
      relvantTypes(v.asType).foreach(s => source.addImport(s))
    })

    return source
  }

  private def createBaseClassSource(typeElement: TypeElement, context: GenerationContext): JavaClassSource = {

    val packageName = context.env.getElementUtils.getPackageOf(typeElement).getQualifiedName.toString
    val name = s"${typeElement.getSimpleName}Properties"

    val source = Roaster.create(classOf[JavaClassSource])
    source.setPackage(packageName)
    source.setName(name)
    source.setPublic()

    if (superclassIsRelevantBean) {
      source.setSuperType(s"${simpleName(typeElement.getSuperclass)}Properties")

      // TODO do not import if same package
      source.addImport(s"${canonicalName(typeElement.getSuperclass)}Properties")
    }

    if (context.suppressAllWarnings)
      source.addAnnotation(classOf[SuppressWarnings]).setStringValue("all")

    source.addAnnotation(classOf[BeanPropertyModel]).setStringValue(typeElement.getQualifiedName.toString)
    source.addAnnotation(classOf[Generated])
      .setStringArrayValue(List(classOf[BeanModelProcessor].getCanonicalName).toArray)
      .setStringValue("date", DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now))

    return source
  }
}

object BeanPropertyModelSourceGenerator {

  def apply(context: GenerationContext)(typeElement: TypeElement) =
    new BeanPropertyModelSourceGenerator(context, typeElement)
}
