package de.maci.beanmodel.generator.testhelper

import org.mockito.Mockito.when

import de.maci.beanmodel.generator.testhelper.NameMocker.mockName
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
final class PackageElementMocker private () extends Mocker[PackageElement] {

  private var _qualifiedName: Option[String] = None

  def withQualifiedName(qualifiedName: String) = { _qualifiedName = Some(qualifiedName); this }

  def build: PackageElement = {
    val instance = mock[PackageElement]
    when(instance.getKind) thenReturn ElementKind.PACKAGE
    when(instance.getQualifiedName) thenAnswer newAnswer((_) => mockName withContent (qualifiedName) build)
    when(instance.getSimpleName) thenAnswer newAnswer((_) => mockName withContent (simpleName) build)
    when(instance.isUnnamed()) thenReturn isQualifiedNameEmpty

    instance
  }

  private def isQualifiedNameEmpty = !_qualifiedName.isDefined || _qualifiedName.get.isEmpty()

  private def qualifiedName = if (!isQualifiedNameEmpty) _qualifiedName.get else ""
  private def simpleName: String = if (!isQualifiedNameEmpty) qualifiedName.split("\\.")(qualifiedName.split("\\.").length - 1) else ""
}

final object PackageElementMocker {

  def mockPackageElement = new PackageElementMocker
}
