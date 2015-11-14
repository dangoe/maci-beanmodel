package de.maci.beanmodel.generator.util

import java.io.Closeable

import org.junit.runner.RunWith
import org.mockito.Mockito.{ times, verify }
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ FlatSpec, Matchers }

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 01.05.15
 */
@RunWith(classOf[JUnitRunner])
final class CloseablesTest extends FlatSpec with Matchers with MockitoSugar {

  "autoclose" should "close the Closable-instance if the callback has been finished" in {

    val someCloseable = mock[Closeable]

    Closeables.autoclose(() => someCloseable,
      (c: Closeable) => {},
      (e: Exception) => {})

    verify(someCloseable, times(1)).close()
  }

  it should "close the Closable-instance if an exception was thrown" in {

    val someCloseable = mock[Closeable]

    Closeables.autoclose(() => someCloseable,
      (c: Closeable) => throw new IllegalStateException(),
      (e: Exception) => {})

    verify(someCloseable, times(1)).close()
  }

  it should "call the exception callback if an exception was thrown" in {

    var exceptionHandlerCalled = false

    Closeables.autoclose(() => mock[Closeable],
      (c: Closeable) => throw new IllegalStateException(),
      (e: Exception) => exceptionHandlerCalled = true)

    exceptionHandlerCalled shouldBe true
  }

  it should "not call the exception callback if no exception was thrown" in {

    var exceptionHandlerCalled = false

    Closeables.autoclose(() => mock[Closeable],
      (c: Closeable) => {},
      (e: Exception) => exceptionHandlerCalled = true)

    exceptionHandlerCalled shouldBe false
  }

  it should """return Some("Value") if everything worked fine""" in {

    Closeables.autoclose(() => mock[Closeable],
      (c: Closeable) => "Value",
      (e: Exception) => {}) shouldBe Some("Value")
  }

  it should "return None if an exception was thrown" in {

    Closeables.autoclose(() => mock[Closeable],
      (c: Closeable) => throw new IllegalStateException(),
      (e: Exception) => {}) shouldBe None
  }
}
