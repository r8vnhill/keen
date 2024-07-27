/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.matchers.shouldHaveFitness
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.arbNonNaNDouble
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
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
                ) { individual ->
                    individual.verify() shouldBe true
                }
            }

            "should return false if the genotype is invalid" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(
                            arbSimpleFeature(Arb.double()),
                            Arb.constant(IsValidRepresentation.INVALID)
                        )
                    )
                ) { individual ->
                    individual.verify().shouldBeFalse()
                }
            }

            "should return false if the fitness is NaN" {
                checkAll(
                    arbIndividual(
                        arbSimpleRepresentation(arbSimpleFeature(Arb.double())),
                        Arb.constant(Double.NaN)
                    )
                ) { individual ->
                    individual.verify() shouldBe false
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

private fun <T, F> arbIndividualAndFlattenedRepresentation(
    feature: Arb<F>
) where F : Feature<T, SimpleFeature<T>> = arbitrary {
    val size = Arb.int(0..10).bind()
    val flattened = mutableListOf<T>()
    val elements = mutableListOf<List<F>>()
    repeat(size) {
        val list = Arb.list(feature, 0..10).bind()
        elements.add(list)
        flattened.addAll(list.map { it.value })
    }
    Individual(object : Representation<T, SimpleFeature<T>> {
        override val size: Int
            get() = flattened.size

        override fun flatten() = flattened
    }) to flattened
}

private fun arbIndividual(
    representation: Arb<Representation<Double, SimpleFeature<Double>>>,
    fitness: Arb<Double> = Arb.double()
): Arb<Individual<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>> = arbitrary {
    Individual(representation.bind(), fitness.bind())
}

private fun <T, F> arbSimpleRepresentation(
    feature: Arb<Feature<T, F>>,
    isValidRepresentation: Arb<IsValidRepresentation> = Arb.enum<IsValidRepresentation>()
): Arb<Representation<T, F>> where F : Feature<T, F> = arbitrary {
    val features = Arb.list(feature).bind()
    when (isValidRepresentation.bind()) {
        IsValidRepresentation.VALID -> object : Representation<T, F> {
            override val size: Int
                get() = features.size

            override fun flatten() = features.map { it.value }

            override fun toString() = features.joinToString(prefix = "[", postfix = "]")
        }

        IsValidRepresentation.INVALID -> object : Representation<T, F> {
            override val size: Int
                get() = features.size

            override fun flatten() = features.map { it.value }

            override fun verify() = false

            override fun toString() = features.joinToString(prefix = "[", postfix = "]")
        }
    }
}

private fun <T> arbSimpleFeature(value: Arb<T>): Arb<SimpleFeature<T>> = value.map {
    SimpleFeature(it)
}

private class SimpleFeature<T>(override val value: T) : Feature<T, SimpleFeature<T>> {
    override fun duplicateWithValue(value: T) = SimpleFeature(value)

    override fun toString() = "$value"
}

private enum class IsValidRepresentation {
    VALID, INVALID
}
