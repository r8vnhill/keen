package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.IndividualShrinker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.RepresentationShrinker
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.map

/**
 * Provides an [Arb] (Arbitrary) generator for creating random [IndividualRecord] instances.
 *
 * @param individualArb An arbitrary generator for [Individual] instances.
 * @param representationShrinker A shrinker for reducing the size of the [Representation] during shrinking.
 * @return An [Arb] that generates random [IndividualRecord] instances.
 */
fun <T, F, R> arbIndividualRecord(
    individualArb: Arb<Individual<T, F, R>>,
    representationShrinker: RepresentationShrinker<T, F, R>
)
        where F : Feature<T, F>,
              R : Representation<T, F> = arbitrary(IndividualRecordShrinker(representationShrinker)) {
    individualArb.map {
        IndividualRecord.fromIndividual(it)
    }.bind()
}

/**
 * A [Shrinker] implementation for [IndividualRecord] instances, which reduces the size of both the representation
 * and fitness fields of an individual.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature used in the representation, which must implement [Feature].
 * @param R The type of representation, which must implement [Representation].
 * @param representationShrinker A shrinker for reducing the size of the [Representation].
 */
class IndividualRecordShrinker<T, F, R>(private val representationShrinker: RepresentationShrinker<T, F, R>) :
        Shrinker<IndividualRecord<T, F, R>> where F : Feature<T, F>,
                                                  R : Representation<T, F> {

    /**
     * Shrinks the given [IndividualRecord] by shrinking its underlying [Individual].
     *
     * @param value The [IndividualRecord] to shrink.
     * @return A list of smaller [IndividualRecord] instances.
     */
    override fun shrink(value: IndividualRecord<T, F, R>) =
        IndividualShrinker(representationShrinker).shrink(value.toIndividual()).map {
            IndividualRecord.fromIndividual(it)
        }
}
