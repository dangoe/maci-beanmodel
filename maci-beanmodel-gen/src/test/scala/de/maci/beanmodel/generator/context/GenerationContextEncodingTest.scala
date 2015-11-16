package de.maci.beanmodel.generator.context

import java.nio.charset.Charset
import javax.annotation.processing.ProcessingEnvironment

import de.maci.beanmodel.generator.context.GenerationContext._
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 14.11.15
  */
@RunWith(classOf[JUnitRunner])
class GenerationContextEncodingTest extends FlatSpec with Matchers with MockitoSugar {

  "Default encoding" should "be UTF-8" in {
    val processingEnvironment = mock[ProcessingEnvironment]
    when(processingEnvironment.getOptions).thenReturn(Map.empty[String, String])

    val sut = GenerationContext(processingEnvironment)

    sut.encoding shouldBe DefaultEncoding
  }

  it should "be UTF-8 if an invalid encoding name has been configured" in {
    val processingEnvironment = mock[ProcessingEnvironment]
    when(processingEnvironment.getOptions).thenReturn(Map((PropertyEncoding, "FooBar")))

    val sut = GenerationContext(processingEnvironment)

    sut.encoding shouldBe DefaultEncoding
  }

  it can "be changed to ISO-8859-1" in {
    val processingEnvironment = mock[ProcessingEnvironment]
    when(processingEnvironment.getOptions).thenReturn(Map((PropertyEncoding, "ISO-8859-1")))

    val sut = GenerationContext(processingEnvironment)

    sut.encoding shouldBe Charset.forName("ISO-8859-1")
  }
}
