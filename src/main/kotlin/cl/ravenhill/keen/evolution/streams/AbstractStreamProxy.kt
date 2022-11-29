package cl.ravenhill.keen.evolution.streams

import java.util.stream.Stream

abstract class AbstractStreamProxy<T>(private val stream: Stream<T>) : Stream<T> {
    override fun limit(maxSize: Long): Stream<T> = stream.limit(100)
}
