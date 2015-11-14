package de.maci.beanmodel.generator.testhelper

import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.mock.MockitoSugar

/**
 * @author Daniel Götten <daniel.goetten@googlemail.com>
 * @since 30.04.15
 */
trait Mocker[T] extends MockitoSugar {

  def build : T

  protected final def newAnswer[S](f: InvocationOnMock => S): Answer[S] =
    new Answer[S] {
      def answer(i: InvocationOnMock): S = f.apply(i)
    }
}
