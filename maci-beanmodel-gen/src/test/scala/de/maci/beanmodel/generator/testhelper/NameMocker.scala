package de.maci.beanmodel.generator.testhelper

import javax.lang.model.element.{ ElementKind, PackageElement, Name }

import de.maci.beanmodel.generator.testhelper.PackageElementMocker._
import org.mockito.Mockito.when

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
final class NameMocker private () extends Mocker[Name] {

  private var _content: Option[String] = None

  def withContent(content: String): this.type = { _content = Some(content); this }

  def build: Name = {
    val instance = mock[Name]
    when(instance.toString) thenReturn _content.orElse(Some("")).get

    instance
  }
}

final object NameMocker {

  def mockName = new NameMocker()
}
