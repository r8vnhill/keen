package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.checkAll

class CombineCrossoverTest : FreeSpec({
    "When constructing" - {
        "throws an exception if the chromosome rate is not between 0 and 1" {
            checkAll(arbInvalidProbability()) { rate ->
                shouldThrow<CompositeException> {
                    CombineCrossover<Int, IntGene>({ genes -> genes.first() }, chromosomeRate = rate)
                }.shouldHaveInfringement<CrossoverConfigException>("The chromosome rate ($rate) must be in 0.0..1.0")
            }
        }

        "throws an exception if the gene rate is not between 0 and 1" {
            checkAll(arbInvalidProbability()) { rate ->
                shouldThrow<CompositeException> {
                    CombineCrossover<Int, IntGene>({ genes -> genes.first() }, geneRate = rate)
                }.shouldHaveInfringement<CrossoverConfigException>("The gene rate ($rate) must be in 0.0..1.0")
            }
        }

        "throws an exception if the number of parents is less than 2" {
            checkAll(Arb.nonPositiveInt()) { numParents ->
                shouldThrow<CompositeException> {
                    CombineCrossover<Int, IntGene>({ genes -> genes.first() }, numParents = numParents)
                }.shouldHaveInfringement<CrossoverConfigException>(
                    "The number of parents ($numParents) must be greater than 1"
                )
            }
        }
    }
})
