package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evaluator
import cl.ravenhill.keen.arbs.evolution.evolutionEngine
import cl.ravenhill.keen.arbs.evolution.fitnessFunction
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.operators.selector
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class EngineTest : FreeSpec({

    "An evolution [Engine]" - {
        "have a [generation] property that" - {
            "starts at 0" {
                checkAll(Arb.engine()) { engine ->
                    engine.generation shouldBe 0
                }
            }
        }
    }
})
