package de.maci.beanmodel.generator.context

import javax.annotation.processing.ProcessingEnvironment

import de.maci.beanmodel.generator.context.GenerationContext.SuppressAllWarnings
import org.mockito.Mockito.when
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.mock.MockitoSugar

import scala.collection.JavaConversions._

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 14.11.15
  */
class GenerationContextSupressWarningsTest extends FlatSpec with Matchers with MockitoSugar {

  "SuppressWarnings annotation" should "be added if option is not set" in {
    val processingEnvironment = mock[ProcessingEnvironment]
    when(processingEnvironment.getOptions).thenReturn(Map.empty[String, String])

    val sut = GenerationContext(processingEnvironment)

    sut.isSuppressAllWarnings shouldBe true
  }

  it should "be added if option is set to 'true'" in {
    val processingEnvironment = mock[ProcessingEnvironment]
    when(processingEnvironment.getOptions).thenReturn(Map((SuppressAllWarnings, "true")))

    val sut = GenerationContext(processingEnvironment)

    sut.isSuppressAllWarnings shouldBe true
  }

  it should "not be added if option is set to 'false'" in {
    val processingEnvironment = mock[ProcessingEnvironment]
    when(processingEnvironment.getOptions).thenReturn(Map((SuppressAllWarnings, "false")))

    val sut = GenerationContext(processingEnvironment)

    sut.isSuppressAllWarnings shouldBe false
  }
}
