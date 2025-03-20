/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package keen

import cl.ravenhill.IsValidRepresentation
import cl.ravenhill.MatrixRepresentation
import cl.ravenhill.SimpleFeature
import cl.ravenhill.SimpleRepresentation
import cl.ravenhill.arbSimpleFeature
import cl.ravenhill.arbSimpleRepresentation
import cl.ravenhill.keen.matchers.shouldBeEvaluated
import cl.ravenhill.keen.matchers.shouldNotBeEvaluated
import cl.ravenhill.keen.matchers.shouldNotBeValid
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.RepresentationShrinker
import cl.ravenhill.matchers.shouldBeValid
import cl.ravenhill.utils.arbIndividual
import cl.ravenhill.utils.arbListOfN
import cl.ravenhill.utils.arbNonNanDouble
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.DoubleShrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({

    afterEach { keen.ResetDomainListener.afterTest() }

    "An Individual instance" - {
        "should have a representation property that is set according to the constructor" {
            checkAll(arbSimpleRepresentation(arbSimpleFeature())) { representation ->
                val individual = cl.ravenhill.keen.Individual(representation)
                individual.representation shouldBe representation
            }
        }

        "should have a fitness property that is set according to the constructor" {
            checkAll(arbSimpleRepresentation(arbSimpleFeature()), arbNonNanDouble()) { representation, fitness ->
                val individual = cl.ravenhill.keen.Individual(representation, fitness)
                individual.fitness shouldBe fitness
            }
        }

        "should have a size property that is equal to the size of the representation" {
            checkAll(arbSimpleRepresentation(arbSimpleFeature())) { representation ->
                val individual = cl.ravenhill.keen.Individual(representation)
                individual.size shouldBe representation.size
            }
        }

        "when verified" - {
            "should return true if the representation is valid" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(
                            arbSimpleFeature(),
                            Arb.constant(IsValidRepresentation.VALID)
                        )
                    )
                ) { individual -> individual.shouldBeValid() }
            }

            "should return false if the representation is invalid" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(
                            arbSimpleFeature(),
                            Arb.constant(IsValidRepresentation.INVALID)
                        )
                    )
                ) { individual -> individual.shouldNotBeValid() }
            }
        }

        "can be flattened" {
            checkAll(
                keen.arbIndividualAndFlattenedRepresentation(
                    Arb.int(0..10),
                    arbSimpleFeature()
                )
            ) { (individual, flattened) ->
                individual.flatten() shouldBe flattened
            }
        }

        "when checking if an individual is evaluated" - {
            "should return true if the fitness is not NaN" {
                checkAll(
                    arbSimpleRepresentation(arbSimpleFeature()),
                    arbNonNanDouble()
                ) { representation, fitness ->
                    val individual = cl.ravenhill.keen.Individual(representation, fitness)
                    individual.shouldBeEvaluated()
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(
                    arbSimpleRepresentation(arbSimpleFeature())
                ) { representation ->
                    val individual = cl.ravenhill.keen.Individual(representation)
                    individual.shouldNotBeEvaluated()
                }
            }
        }

        "can apply a fold left operation" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature()),
                arbNonNanDouble(),
                Arb.int()
            ) { representation, fitness, initial ->
                val individual = cl.ravenhill.keen.Individual(representation, fitness)
                val result = individual.fold(initial) { acc, v -> acc + v }
                val expected = representation.fold(initial) { acc, v -> acc + v }
                result shouldBe expected
            }
        }

        "can apply a fold right operation" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature()),
                arbNonNanDouble(),
                Arb.int()
            ) { representation, fitness, initial ->
                val individual = cl.ravenhill.keen.Individual(representation, fitness)
                val result = individual.foldRight(initial) { v, acc -> acc + v }
                val expected = representation.foldRight(initial) { v, acc -> acc + v }
                result shouldBe expected
            }
        }

        "can be converted to a string when" - {
            "using the simple mode" {
                val representation =
                    SimpleRepresentation(listOf(SimpleFeature(1), SimpleFeature(2)), IsValidRepresentation.VALID)
                val individual = cl.ravenhill.keen.Individual(representation, 0.5)
                cl.ravenhill.keen.Domain.toStringMode = cl.ravenhill.keen.ToStringMode.SIMPLE
                individual.toString() shouldBe
                        "SimpleRepresentation(features=[SimpleFeature(value=1), SimpleFeature(value=2)], " +
                        "isValid=VALID) -> 0.5"
            }

            "using the detailed mode" {
                val representation =
                    SimpleRepresentation(listOf(SimpleFeature(1), SimpleFeature(2)), IsValidRepresentation.VALID)
                val individual = cl.ravenhill.keen.Individual(representation, 0.5)
                cl.ravenhill.keen.Domain.toStringMode = cl.ravenhill.keen.ToStringMode.DEFAULT
                individual.toString() shouldBe
                        "Individual(representation=SimpleRepresentation(features=[SimpleFeature(value=1), " +
                        "SimpleFeature(value=2)], isValid=VALID), fitness=0.5)"
            }
        }

        "should have an equals method that" - {
            "is reflexive" {
                checkAll(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))) { individual ->
                    individual shouldBe individual
                }
            }

            "is symmetric" {
                checkAll(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))) { individual ->
                    val copy = individual.copy()
                    individual shouldBe copy
                    copy shouldBe individual
                }
            }

            "is transitive" {
                checkAll(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))) { individual ->
                    val copy1 = individual.copy()
                    val copy2 = individual.copy()
                    individual shouldBe copy1
                    copy1 shouldBe copy2
                    individual shouldBe copy2
                }
            }

            "returns false when comparing with a different individual" {
                checkAll(
                    PropTestConfig(iterations = 50), // Use a reduced number of iterations because of assume
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature())),
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                ) { individual1, individual2 ->
                    assume { individual1.representation shouldNotBe individual2.representation }
                    individual1 shouldNotBe individual2
                }
            }
        }

        "should have a hashCode method that" - {
            "returns the same hash code for equal individuals" {
                checkAll(arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))) { individual ->
                    val copy = individual.copy()
                    individual.hashCode() shouldBe copy.hashCode()
                }
            }

            "returns different hash codes for different individuals" {
                checkAll(
                    PropTestConfig(iterations = 50), // Use a reduced number of iterations because of assume
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature())),
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
                ) { individual1, individual2 ->
                    assume { individual1.representation shouldNotBe individual2.representation }
                    individual1.hashCode() shouldNotBe individual2.hashCode()
                }
            }
        }
    }
})

/**
 * Generates a pair of an `Individual` and its corresponding flattened representation.
 *
 * @param arbSize An `Arb<Int>` generator that determines the size of the feature list.
 * @param arbFeature An `Arb<F>` generator that generates the features to be included in the representation.
 * @return A generator that produces pairs of an `Individual` and its flattened list of values.
 */
private fun <T, F, R> arbIndividualAndFlattenedRepresentation(
    arbSize: Arb<Int>,
    arbFeature: Arb<F>
) where F : Feature<T, F>,
        R : Representation<T, F> = arbitrary {
    val size = arbSize.bind()
    val elements = arbListOfN(size, Arb.list(arbFeature)).bind()
    val flattened = elements.flatten().map { it.value }
    val representation = MatrixRepresentation(elements)
    cl.ravenhill.keen.Individual(representation) to flattened
}

/**
 * Provides an [Arb] (Arbitrary) generator for creating random [Individual] instances, combining a representation and a
 * fitness value.
 *
 * @param representationArb An arbitrary generator for [Representation] instances.
 * @param fitnessArb An arbitrary generator for fitness values, defaulting to non-NaN doubles.
 * @param representationShrinker A shrinker for reducing the size of the [Representation] during shrinking.
 * @return An [Arb] that generates random [Individual] instances.
 */
fun <T, F, R> arbIndividual(
    representationArb: Arb<R>,
    fitnessArb: Arb<Double> = arbNonNanDouble(),
    representationShrinker: RepresentationShrinker<T, F, R>
)
        where F : Feature<T, F>,
              R : Representation<T, F> = arbitrary(keen.IndividualShrinker(representationShrinker)) {
    Arb.bind(representationArb, fitnessArb) { representation, fitness ->
        cl.ravenhill.keen.Individual(representation, fitness)
    }.bind()
}

/**
 * A [Shrinker] implementation for [Individual] instances, shrinking both the representation and fitness.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature used in the representation, which must implement [Feature].
 * @param R The type of representation, which must implement [Representation].
 * @param representationShrinker A shrinker for reducing the size of the [Representation].
 */
class IndividualShrinker<T, F, R>(
    private val representationShrinker: RepresentationShrinker<T, F, R>
) : Shrinker<cl.ravenhill.keen.Individual<T, F, R>> where F : Feature<T, F>,
                                                          R : Representation<T, F> {

    /**
     * Shrinks the given [Individual] by shrinking both its representation and fitness.
     *
     * @param value The [Individual] to shrink.
     * @return A list of smaller [Individual] instances.
     */
    override fun shrink(value: cl.ravenhill.keen.Individual<T, F, R>) =
        representationShrinker.shrink(value.representation).map {
            DoubleShrinker.shrink(value.fitness).map { fitness ->
                cl.ravenhill.keen.Individual(it, fitness)
            }
        }.flatten()
}
