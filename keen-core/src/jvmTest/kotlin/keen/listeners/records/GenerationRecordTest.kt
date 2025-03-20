package keen.listeners.records

import cl.ravenhill.SimpleFeature
import cl.ravenhill.SimpleRepresentation
import cl.ravenhill.arbSimpleRepresentation
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleRepresentationShrinker
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.matchers.shouldContainExceptionOfType
import cl.ravenhill.utils.arbIndividual
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.IntShrinker
import io.kotest.property.arbitrary.ListShrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class GenerationRecordTest : FreeSpec({
    "A GenerationRecord" - {
        "should not allow negative generation numbers" {
            checkAll(Arb.negativeInt()) { generation ->
                shouldThrow<CompositeException> {
                    GenerationRecord<_, _, SimpleRepresentation<Int, SimpleFeature>>(generation)
                }.shouldContainExceptionOfType<IntConstraintException>(
                    "The generation number ($generation) must not be negative"
                )
            }
        }

        "should not allow a negative number of steady generations" {
            checkAll(arbGenerationRecord(), Arb.negativeInt()) { record, steady ->
                shouldThrowUnit<CompositeException> {
                    record.steady = steady
                }.shouldContainExceptionOfType<IntConstraintException>(
                    "The steady counter ($steady) must not be negative"
                )
            }
        }
    }
})

/**
 * Provides an [Arb] (Arbitrary) generator for creating random [GenerationRecord] instances with a non-negative integer
 * generation and a population of individuals.
 *
 * @return An [Arb] that generates a [GenerationRecord] with a non-negative integer generation.
 */
fun arbGenerationRecord() =
    arbitrary(GenerationRecordShrinker()) {
        Arb.nonNegativeInt()
            .map { GenerationRecord<_, _, SimpleRepresentation<Int, SimpleFeature>>(it) }
            .map {
                val representationArb = arbSimpleRepresentation(arbSimpleFeature())
                val representationShrinker = SimpleRepresentationShrinker<_, SimpleFeature>()
                it.population.parents = Arb.list(
                    arbIndividualRecord(
                        arbIndividual(
                            representationArb,
                            Arb.double()
                        ),
                        representationShrinker
                    )
                ).next()
                it.population.offspring = Arb.list(
                    arbIndividualRecord(
                        arbIndividual(
                            representationArb,
                            Arb.double()
                        ),
                        representationShrinker
                    )
                ).next()
                it
            }
            .bind()
    }

/**
 * A [Shrinker] implementation for shrinking [GenerationRecord] instances. It attempts to shrink the generation
 * field by reducing its value, producing smaller [GenerationRecord] instances.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature used in the representation, which must implement [Feature].
 * @param R The type of representation used, which must implement [Representation].
 */
class GenerationRecordShrinker<T, F, R> : Shrinker<GenerationRecord<T, F, R>>
        where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Shrinks the given [GenerationRecord] by reducing its generation field.
     *
     * @param value The [GenerationRecord] to shrink.
     * @return A list of smaller [GenerationRecord] instances.
     */
    override fun shrink(value: GenerationRecord<T, F, R>): List<GenerationRecord<T, F, R>> =
        IntShrinker(0..Int.MAX_VALUE)
            .shrink(value.generation).map { generation ->
                ListShrinker<IndividualRecord<T, F, R>>(0..100)
                    .shrink(value.population.parents).map { parents ->
                        ListShrinker<IndividualRecord<T, F, R>>(0..100)
                            .shrink(value.population.offspring).map { offspring ->
                                GenerationRecord<T, F, R>(generation).apply {
                                    population.parents = parents
                                    population.offspring = offspring
                                }
                            }
                    }.flatten()
            }.flatten()
}
