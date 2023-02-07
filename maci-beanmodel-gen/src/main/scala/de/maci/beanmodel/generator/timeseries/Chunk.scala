package de.maci.beanmodel.generator.timeseries

/**
  * @author Daniel Götten <daniel.goetten@googlemail.com>
  * @since 24.11.15
  */
class Chunk(begin: Long, gridSize: Int) {

  def fill(values: Array[Value]) = {
    assert(values.length == gridSize, "Values must be of size " + gridSize + "!")

  }
}

class Value(val value: Long) {

  def +(other: Value) = value + other.value

  def -(other: Value) = value - other.value

  def *(other: Value) = value * other.value
}
