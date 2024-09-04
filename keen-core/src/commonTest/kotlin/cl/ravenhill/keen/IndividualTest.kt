/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.matchers.shouldBeEvaluated
import cl.ravenhill.keen.matchers.shouldNotBeEvaluated
import cl.ravenhill.keen.matchers.shouldNotBeValid
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeatureShrinker
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
import io.kotest.property.arbitrary.ListShrinker
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({

    afterEach { ResetDomainListener.afterTest() }

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
                    arbSimpleFeature(),
                    MatrixRepresentationShrinker(SimpleFeatureShrinker())
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

        "can be converted to a string when" - {
            "using the simple mode" {
                val representation =
                    SimpleRepresentation(listOf(SimpleFeature(1), SimpleFeature(2)), IsValidRepresentation.VALID)
                val individual = Individual(representation, 0.5)
                Domain.toStringMode = ToStringMode.SIMPLE
                individual.toString() shouldBe
                        "SimpleRepresentation(features=[SimpleFeature(value=1), SimpleFeature(value=2)], " +
                        "isValid=VALID) -> 0.5"
            }

            "using the detailed mode" {
                val representation =
                    SimpleRepresentation(listOf(SimpleFeature(1), SimpleFeature(2)), IsValidRepresentation.VALID)
                val individual = Individual(representation, 0.5)
                Domain.toStringMode = ToStringMode.DEFAULT
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
private fun <T, F> arbIndividualAndFlattenedRepresentation(
    arbSize: Arb<Int>,
    arbFeature: Arb<F>,
    representationShrinker: RepresentationShrinker<T, F, MatrixRepresentation<T, F>>
) where F : Feature<T, F> =
    arbitrary(IndividualAndFlattenedRepresentationShrinker(representationShrinker)) {
        val size = arbSize.bind()
        val elements = arbListOfN(size, Arb.list(arbFeature)).bind()
        val flattened = elements.flatten().map { it.value }
        val representation = MatrixRepresentation(elements)
        Individual(representation) to flattened
    }

class IndividualAndFlattenedRepresentationShrinker<T, F, R>(
    private val representationShrinker: Shrinker<R>,
    private val shrinkingRange: IntRange = 0..100
) : Shrinker<Pair<Individual<T, F, R>, List<T>>> where F : Feature<T, F>, R : Representation<T, F> {

    override fun shrink(value: Pair<Individual<T, F, R>, List<T>>): List<Pair<Individual<T, F, R>, List<T>>> {
        val (individual, flattened) = value
        val shrunkRepresentations = representationShrinker.shrink(individual.representation)
            .map { shrunkRepresentation ->
                val newFlattened = shrunkRepresentation.flatten()
                Individual(shrunkRepresentation) to newFlattened
            }

        // Additionally shrink the flattened list by shrinking values in the flattened list directly
        val shrunkFlattenedLists = ListShrinker<T>(shrinkingRange).shrink(flattened).map { it.toList() }

        // Combine both shrink attempts
        val shrunkPairs = shrunkFlattenedLists.map { newFlattened ->
            individual to newFlattened
        }

        return shrunkRepresentations + shrunkPairs
    }
}
