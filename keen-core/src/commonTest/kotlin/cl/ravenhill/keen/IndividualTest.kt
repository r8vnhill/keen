/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.matchers.shouldBeEq
import cl.ravenhill.keen.matchers.shouldHaveFitness
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.IsValidRepresentation
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import cl.ravenhill.keen.utils.arbNonNaNDouble
import cl.ravenhill.matchers.shouldBeValid
import cl.ravenhill.matchers.shouldNotBeValid
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class IndividualTest : FreeSpec({


    beforeEach {
        Domain.toStringMode = ToStringMode.DEFAULT
    }

    "An Individual instance" - {
        "should have a representation property that is set according to the constructor" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                Arb.double()
            ) { representation, fitness ->
                val individual = Individual(representation, fitness)
                individual.representation shouldBe representation
            }
        }

        "should have a fitness property that is set according to the constructor" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                arbNonNaNDouble()
            ) { representation, fitness ->
                val individual = Individual(representation, fitness)
                individual shouldHaveFitness fitness
            }
        }

        "should have a size property that is equal to the size of the representation" {
            checkAll(
                arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                Arb.double()
            ) { representation, fitness ->
                val individual = Individual(representation, fitness)
                individual.size shouldBe representation.size
            }
        }

        "when verifying" - {
            "should return true if the genotype is valid and the fitness is not NaN" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(
                            arbSimpleFeature(Arb.double()),
                            Arb.constant(IsValidRepresentation.VALID)
                        ),
                        arbNonNaNDouble()
                    )
                ) { individual -> individual.shouldBeValid() }
            }

            "should return false if the genotype is invalid" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(
                            arbSimpleFeature(Arb.double()),
                            Arb.constant(IsValidRepresentation.INVALID)
                        )
                    )
                ) { individual -> individual.shouldNotBeValid() }
            }

            "should return false if the fitness is NaN" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                        Arb.constant(Double.NaN)
                    )
                ) { individual ->
                    individual.shouldNotBeValid()
                }
            }
        }

        "can be flattened" {
            checkAll(
                arbIndividualAndFlattenedRepresentation(arbSimpleFeature(Arb.double()))
            ) { (individual, flattened) ->
                val flatMapped = individual.flatten()
                flatMapped.size shouldBe flattened.size
                flatMapped shouldBe flattened
            }
        }

        "when checking if the individual is evaluated" - {
            "should return true if the fitness is not NaN" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                        arbNonNaNDouble()
                    )
                ) { individual ->
                    individual.isEvaluated() shouldBe true
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                        Arb.constant(Double.NaN)
                    )
                ) { individual ->
                    individual.isEvaluated() shouldBe false
                }
            }
        }

        "can apply a fold operation" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainListener)),
                arbIndividual(
                    arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                    Arb.double()
                ),
                Arb.double(-1000.0, 1000.0),
                Arb.double(-1000.0, 1000.0)
            ) { individual, initial, value ->
                Domain.equalityThreshold = 0.1
                val result = individual.fold(initial) { acc, _ -> acc + value }
                result shouldBeEq individual.size * value + initial
            }
        }

        "can be converted to" - {
            "a simple string" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual ->
                    Domain.toStringMode = ToStringMode.SIMPLE
                    individual.toString() shouldBe "${individual.representation} -> ${individual.fitness}"
                }
            }

            "a detailed string" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual ->
                    individual.toString() shouldBe
                            "Individual(representation=${individual.representation}, fitness=${individual.fitness})"
                }
            }
        }

        "has equality that" - {
            "is reflexive" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual ->
                    individual shouldBe individual
                }
            }

            "is symmetric" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual ->
                    val other = individual.copy()
                    individual shouldBe other
                    other shouldBe individual
                }
            }

            "is transitive" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual ->
                    val other = individual.copy()
                    val another = other.copy()
                    individual shouldBe other
                    other shouldBe another
                    individual shouldBe another
                }
            }

            "is false when the representations are different" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual, other ->
                    assume {
                        individual.representation shouldNotBe other.representation
                    }
                    individual shouldNotBe other
                }
            }
        }

        "has a hash code that" - {
            "is consistent with equals" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual ->
                    val other = individual.copy()
                    individual shouldHaveSameHashCodeAs other
                }
            }

            "is different when the representations are different" {
                checkAll(
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
                    arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double())))
                ) { individual, other ->
                    assume {
                        individual.representation shouldNotBe other.representation
                    }
                    individual shouldNotHaveSameHashCodeAs other
                }
            }
        }
    }
})

/**
 * Generates an [Individual] and its flattened representation.
 *
 * This function creates an [Individual] with a corresponding [Representation] where the representation is constructed
 * by combining several lists of features. It also returns the flattened list of values.
 *
 * @param feature The arbitrary generator for features used in the representation.
 * @param T The type of value stored by the feature.
 * @param F The kind of feature used in the representation, which must implement [Feature].
 *
 * @return A pair consisting of an [Individual] and a list of values representing the flattened form of the
 *   representation.
 */
private fun <T, F> arbIndividualAndFlattenedRepresentation(
    feature: Arb<F>,
) where F : Feature<T, SimpleFeature<T>> = arbitrary {
    // Generate a random size for the number of feature lists
    val size = Arb.int(0..10).bind()
    val flattened = mutableListOf<T>() // List to store the flattened values
    val elements = mutableListOf<List<F>>() // List to store the feature lists

    // Create feature lists and populate the flattened list
    repeat(size) {
        val list = Arb.list(feature, 0..10).bind()
        elements.add(list)
        flattened.addAll(list.map { it.value })
    }

    // Create a Representation with the flattened values
    Individual(object : Representation<T, SimpleFeature<T>> {
        override val size: Int
            get() = flattened.size

        override fun flatten() = flattened
        override fun <R> fold(initial: R, operation: (R, T) -> R): R = flattened.fold(initial, operation)
    }) to flattened
}

/**
 * Generates an [Individual] with a given [Representation] and fitness value.
 *
 * This function creates an [Individual] using a randomly generated representation and fitness value.
 *
 * @param representation The arbitrary generator for the [Representation] of the individual.
 * @param fitness The arbitrary generator for the fitness value of the individual.
 *
 * @return An [Individual] instance with the generated representation and fitness value.
 */
fun arbIndividual(
    representation: Arb<Representation<Double, SimpleFeature<Double>>>,
    fitness: Arb<Double> = Arb.double(),
): Arb<Individual<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>> = arbitrary {
    Individual(representation.bind(), fitness.bind())
}
