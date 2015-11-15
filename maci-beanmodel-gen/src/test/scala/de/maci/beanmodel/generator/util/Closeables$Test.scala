package de.maci.beanmodel.generator.util

import java.io.Closeable

import de.maci.beanmodel.generator.util.Closeables.consume
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 14.11.15
  */
@RunWith(classOf[JUnitRunner])
class Closeables$Test extends FlatSpec with Matchers with MockitoSugar {

  "consume" should "close the closeable if the block has been finished" in {

    val someCloseable = mock[Closeable]

    consume(someCloseable)(c => Unit)

    verify(someCloseable).close()
  }

  it should "close the closeable if an exception is thrown within the block" in {

    val someCloseable = mock[Closeable]

    try {
      consume(someCloseable)(c => throw new IllegalStateException())
    } catch {
      case e: Exception =>
    }

    verify(someCloseable).close()
  }

  it should """return Some("Value") if everything worked fine""" in {
    consume(mock[Closeable])(c => "Value") shouldBe "Value"
  }
}
