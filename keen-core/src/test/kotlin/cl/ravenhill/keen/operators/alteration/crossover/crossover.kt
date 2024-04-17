package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

suspend fun FreeSpecContainerScope.`throws an exception if the chromosome rate is not in the range 0 to 1`(
    createCrossover: (Double) -> Crossover<Int, IntGene>
) {
    "throws an exception if the chromosome rate is not in the range [0.0, 1.0]" {
        checkAll(arbInvalidProbability()) { rate ->
            shouldThrow<CompositeException> {
                createCrossover(rate)
            }.shouldHaveInfringement<CrossoverConfigException>("The chromosome rate ($rate) must be in 0.0..1.0")
        }
    }
}

