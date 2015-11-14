/**
 * Copyright (c) 2015 Daniel Götten
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
package de.maci.beanmodel.generator.core

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import de.maci.beanmodel.generator.testhelper.NameMocker._
import de.maci.beanmodel.generator.testhelper.PackageElementMocker._
import de.maci.beanmodel.generator.testhelper.TypeElementMocker._
import javax.lang.model.element.ElementKind

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 26.04.15
 */
@RunWith(classOf[JUnitRunner])
final class ImportCollectorTest extends FlatSpec with Matchers {

  private val packageElement = mockPackageElement withQualifiedName ("de.maci") build

  "The collector " should "ensure that a type element is inserted if missing." in {
    val typeElement = mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => packageElement) withSimpleName ("SomeName") build

    val importCollector = ImportCollector(mockPackageElement withQualifiedName ("de.maci.subpackage") build)

    importCollector.append(typeElement)

    importCollector.typeElements should contain theSameElementsAs Set(typeElement)
  }

  it should "ensure that a type element is not added if same package." in {
    val typeElement = mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => packageElement) withSimpleName ("SomeName") build

    val importCollector = ImportCollector(packageElement)

    importCollector.append(typeElement)

    importCollector.typeElements shouldBe empty
  }

  it should "be possible to append a new type element without passing an empty set." in {
    val typeElement = mockTypeElement withKind (ElementKind.CLASS) withEnclosingElement (() => packageElement) withSimpleName ("SomeName") build

    val importCollector = ImportCollector(mockPackageElement withQualifiedName ("de.maci.subpackage") build)

    importCollector.append(typeElement)

    importCollector.typeElements should contain theSameElementsAs (Set(typeElement))
  }
}
