package de.maci.beanmodel.generator.core.conversion

import javax.lang.model.element.Name

import de.maci.beanmodel.generator.core.conversion.ImplicitConversions.nameToString
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}


/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 14.11.15
  */
@RunWith(classOf[JUnitRunner])
class ImplicitConversions$Test extends FlatSpec with Matchers with MockitoSugar {

  "nameToString" should "convert Name to toString value" in {
    val name = mock[Name]
    when(name.toString).thenReturn("someName")

    nameToString(name) shouldBe "someName"
  }
}
