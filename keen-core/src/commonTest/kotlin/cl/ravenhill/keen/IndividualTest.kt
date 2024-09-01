/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.matchers.shouldBeEvaluated
import cl.ravenhill.keen.matchers.shouldNotBeEvaluated
import cl.ravenhill.keen.matchers.shouldNotBeValid
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.IsValidRepresentation
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import cl.ravenhill.matchers.shouldBeValid
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({
    "An Individual instance" - {
        "should have a representation property that is set according to the constructor" {
            checkAll(arbSimpleRepresentation(arbSimpleFeature())) { representation ->
                val individual = Individual(representation)
                individual.representation shouldBe representation
            }
        }

        "should have a fitness property that is set according to the constructor" {
            checkAll(arbSimpleRepresentation(arbSimpleFeature()), arbNonNanDouble()) { representation, fitness ->
                val individual = Individual(representation, fitness)
                individual.fitness shouldBe fitness
            }
        }

        "should have a size property that is equal to the size of the representation" {
            checkAll(arbSimpleRepresentation(arbSimpleFeature())) { representation ->
                val individual = Individual(representation)
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
                arbIndividualAndFlattenedRepresentation(
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
                    val individual = Individual(representation, fitness)
                    individual.shouldBeEvaluated()
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(
                    arbSimpleRepresentation(arbSimpleFeature())
                ) { representation ->
                    val individual = Individual(representation)
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
                val individual = Individual(representation, fitness)
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
                val individual = Individual(representation, fitness)
                val result = individual.foldRight(initial) { v, acc -> acc + v }
                val expected = representation.foldRight(initial) { v, acc -> acc + v }
                result shouldBe expected
            }
        }
    }
})

/**
 * Generates an arbitrary `Individual` for use in evolutionary algorithms.
 *
 * @param arbRepresentation An `Arb<R>` generator for creating random representations.
 * @param arbFitness An optional `Arb<Double>` generator for creating random fitness values. Defaults to
 *   `arbNonNanDouble()`.
 * @return An `Arb<Individual<T, F, R>>` generator that produces random `Individual` instances.
 */
fun <T, F, R> arbIndividual(
    arbRepresentation: Arb<R>,
    arbFitness: Arb<Double> = arbNonNanDouble()
): Arb<Individual<T, F, R>> where F : Feature<T, F>,
                                  R : Representation<T, F> = Arb.bind(arbRepresentation, arbFitness, ::Individual)

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
    val representation = object : Representation<T, F> {

        override val size: Int = flattened.size

        override fun flatten(): List<T> = flattened

        override fun <R> foldRight(initial: R, operation: (T, R) -> R) = flattened.foldRight(initial, operation)

        override fun <R> fold(initial: R, operation: (R, T) -> R) = flattened.fold(initial, operation)
    }
    Individual(representation) to flattened
}
