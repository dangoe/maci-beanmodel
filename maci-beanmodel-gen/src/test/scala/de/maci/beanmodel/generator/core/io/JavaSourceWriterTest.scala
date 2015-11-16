package de.maci.beanmodel.generator.core.io

import java.io.{IOException, OutputStream}
import java.nio.charset.Charset
import javax.annotation.processing.{Filer, Messager}
import javax.tools.Diagnostic.Kind
import javax.tools.JavaFileObject

import de.maci.beanmodel.generator.context.GenerationContext
import org.jboss.forge.roaster.model.source.JavaSource
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.anyString
import org.mockito.Mockito.{verify, when}
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 14.11.15
  */
@RunWith(classOf[JUnitRunner])
class JavaSourceWriterTest extends FlatSpec with Matchers with MockitoSugar {

  "JavaSourceWriter" should "handle all IO exceptions by printing them using the environmental messager" in {
    val messagerMock = mock[Messager]
    val sut = new JavaSourceWriter(generationContext) {

      override def filer: Filer = exceptionThrowingFiler(Unit => new IOException("Some message"))

      override def messager = messagerMock
    }

    sut.write(mock[JavaSource[Nothing]])

    val kindCaptor: ArgumentCaptor[Kind] = ArgumentCaptor.forClass(classOf[Kind])
    val messageCaptor: ArgumentCaptor[String] = ArgumentCaptor.forClass(classOf[String])

    verify(messagerMock).printMessage(kindCaptor.capture(), messageCaptor.capture())

    kindCaptor.getValue shouldBe Kind.ERROR
    assert(messageCaptor.getValue.startsWith("java.io.IOException: Some message"))
  }

  it should "handle all runtime exceptions by printing them using the environmental messager" in {
    val messagerMock = mock[Messager]
    val sut = new JavaSourceWriter(generationContext) {

      override def filer: Filer = exceptionThrowingFiler(Unit => new RuntimeException("Some message"))

      override def messager = messagerMock
    }

    sut.write(mock[JavaSource[Nothing]])

    val kindCaptor: ArgumentCaptor[Kind] = ArgumentCaptor.forClass(classOf[Kind])
    val messageCaptor: ArgumentCaptor[String] = ArgumentCaptor.forClass(classOf[String])

    verify(messagerMock).printMessage(kindCaptor.capture(), messageCaptor.capture())

    kindCaptor.getValue shouldBe Kind.ERROR
    assert(messageCaptor.getValue.startsWith("java.lang.RuntimeException: Some message"))
  }

  it should "write the source to the corresponding output stream" in {
    val outputStream = mock[OutputStream]

    val sut = new JavaSourceWriter(generationContext) {

      override def filer: Filer = {
        val fileObject = mock[JavaFileObject]
        when(fileObject.openOutputStream()).thenReturn(outputStream)

        val filer = mock[Filer]
        when(filer.createSourceFile(anyString)).thenReturn(fileObject)

        return filer
      }

      override def messager = mock[Messager]
    }

    val javaSource = mock[JavaSource[Nothing]]
    when(javaSource.toString).thenReturn("Java source file content")

    sut.write(javaSource)

    verify(outputStream).write("Java source file content".getBytes("UTF-8"))
  }

  private def exceptionThrowingFiler(exceptionFactory: Unit => Exception): Filer = {
    val fileObject = mock[JavaFileObject]
    when(fileObject.openOutputStream()).thenThrow(exceptionFactory.apply())

    val filer = mock[Filer]
    when(filer.createSourceFile(anyString)).thenReturn(fileObject)

    return filer
  }

  private def generationContext: GenerationContext = {
    val context = mock[GenerationContext]
    when(context.encoding).thenReturn(Charset.forName("UTF-8"))
    return context
  }
}
