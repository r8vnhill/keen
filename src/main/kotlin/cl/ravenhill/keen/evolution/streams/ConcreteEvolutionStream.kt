package cl.ravenhill.keen.evolution.streams

import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.EvolutionStart
import cl.ravenhill.keen.evolution.Evolver
import java.util.Optional
import java.util.Spliterator
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.BinaryOperator
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.IntFunction
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.function.ToDoubleFunction
import java.util.function.ToIntFunction
import java.util.function.ToLongFunction
import java.util.stream.Collector
import java.util.stream.DoubleStream
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import java.util.stream.StreamSupport

class ConcreteEvolutionStream<DNA>(
    private val spliterator: Spliterator<EvolutionResult<DNA>>,
    isParallel: Boolean = false
) : AbstractStreamProxy<EvolutionResult<DNA>>(StreamSupport.stream(spliterator, isParallel)),
        EvolutionStream<DNA> {

    constructor(start: () -> EvolutionStart<DNA>, evolver: Evolver<DNA>) : this(
        EvolutionSpliterator(
            start,
            evolver
        )
    )

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun spliterator(): Spliterator<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun isParallel(): Boolean {
        TODO("Not yet implemented")
    }

    override fun sequential(): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun parallel(): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun unordered(): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun onClose(closeHandler: Runnable?): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun distinct(): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun sorted(): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun skip(n: Long): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun toArray(): Array<Any> {
        TODO("Not yet implemented")
    }

    override fun <A : Any?> toArray(generator: IntFunction<Array<A>>?): Array<A> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun findFirst(): Optional<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun findAny(): Optional<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun noneMatch(predicate: Predicate<in EvolutionResult<DNA>>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun allMatch(predicate: Predicate<in EvolutionResult<DNA>>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun anyMatch(predicate: Predicate<in EvolutionResult<DNA>>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun max(comparator: Comparator<in EvolutionResult<DNA>>?): Optional<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun min(comparator: Comparator<in EvolutionResult<DNA>>?): Optional<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun <R : Any?, A : Any?> collect(collector: Collector<in EvolutionResult<DNA>, A, R>?): R {
        TODO("Not yet implemented")
    }

    override fun <R : Any?> collect(
        supplier: Supplier<R>?,
        accumulator: BiConsumer<R, in EvolutionResult<DNA>>?,
        combiner: BiConsumer<R, R>?
    ): R {
        TODO("Not yet implemented")
    }

    override fun <U : Any?> reduce(
        identity: U,
        accumulator: BiFunction<U, in EvolutionResult<DNA>, U>?,
        combiner: BinaryOperator<U>?
    ): U {
        TODO("Not yet implemented")
    }

    override fun reduce(accumulator: BinaryOperator<EvolutionResult<DNA>>?): Optional<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun reduce(
        identity: EvolutionResult<DNA>?,
        accumulator: BinaryOperator<EvolutionResult<DNA>>?
    ): EvolutionResult<DNA> {
        TODO("Not yet implemented")
    }

    override fun forEachOrdered(action: Consumer<in EvolutionResult<DNA>>?) {
        TODO("Not yet implemented")
    }

    override fun forEach(action: Consumer<in EvolutionResult<DNA>>?) {
        TODO("Not yet implemented")
    }

    override fun peek(action: Consumer<in EvolutionResult<DNA>>?): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun sorted(comparator: Comparator<in EvolutionResult<DNA>>?): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }

    override fun flatMapToDouble(mapper: Function<in EvolutionResult<DNA>, out DoubleStream>?): DoubleStream {
        TODO("Not yet implemented")
    }

    override fun flatMapToLong(mapper: Function<in EvolutionResult<DNA>, out LongStream>?): LongStream {
        TODO("Not yet implemented")
    }

    override fun flatMapToInt(mapper: Function<in EvolutionResult<DNA>, out IntStream>?): IntStream {
        TODO("Not yet implemented")
    }

    override fun <R : Any?> flatMap(mapper: Function<in EvolutionResult<DNA>, out Stream<out R>>?): Stream<R> {
        TODO("Not yet implemented")
    }

    override fun mapToDouble(mapper: ToDoubleFunction<in EvolutionResult<DNA>>?): DoubleStream {
        TODO("Not yet implemented")
    }

    override fun mapToLong(mapper: ToLongFunction<in EvolutionResult<DNA>>?): LongStream {
        TODO("Not yet implemented")
    }

    override fun mapToInt(mapper: ToIntFunction<in EvolutionResult<DNA>>?): IntStream {
        TODO("Not yet implemented")
    }

    override fun <R : Any?> map(mapper: Function<in EvolutionResult<DNA>, out R>?): Stream<R> {
        TODO("Not yet implemented")
    }

    override fun filter(predicate: Predicate<in EvolutionResult<DNA>>?): Stream<EvolutionResult<DNA>> {
        TODO("Not yet implemented")
    }
}
