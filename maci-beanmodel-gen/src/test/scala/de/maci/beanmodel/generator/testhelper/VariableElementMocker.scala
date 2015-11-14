package de.maci.beanmodel.generator.testhelper

import javax.lang.model.element._
import de.maci.beanmodel.generator.testhelper.NameMocker._
import org.mockito.Mockito.when
import scala.collection.JavaConverters
import scala.collection.mutable.Builder

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
final class VariableElementMocker private () extends Mocker[VariableElement] {

  private var _kind: Option[ElementKind] = None
  private var _enclosingElement: Option[() => QualifiedNameable] = None
  private var _simpleName: Option[String] = None
  private val _modifiersBuilder: Builder[Modifier, Set[Modifier]] = Set.newBuilder[Modifier]

  def withKind(kind: ElementKind): this.type = { _kind = Some(kind); this }

  def withEnclosingElement(element: () => QualifiedNameable): this.type = { _enclosingElement = Some(element); this }

  def withSimpleName(name: String): this.type = {
    require(!name.isEmpty(), "Simple name must not be empty!")

    _simpleName = Some(name);

    this
  }

  def withModifiers(modifiers: Modifier*): this.type = { _modifiersBuilder.clear(); _modifiersBuilder ++= modifiers; this }

  override def build: VariableElement = {
    assume(_enclosingElement.isDefined, "Enclosing element must be set!")
    assume(_simpleName.isDefined, "Simple name must be set!")

    val instance = mock[VariableElement]
    assume(_kind.isDefined, "ElementKind must be set!")
    when(instance.getKind) thenReturn _kind.get
    when(instance.getEnclosingElement) thenAnswer newAnswer((_) => _enclosingElement.get.apply())
    when(instance.getSimpleName) thenAnswer newAnswer((_) => mockName withContent (_simpleName.get) build)
    when(instance.getModifiers) thenAnswer newAnswer((_) => JavaConverters.setAsJavaSetConverter(_modifiersBuilder.result()).asJava)

    instance
  }
}

final object VariableElementMocker {

  def mockVariableElement = new VariableElementMocker
}
