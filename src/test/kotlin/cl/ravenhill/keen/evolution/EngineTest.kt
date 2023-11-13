package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.evolution.evaluator
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.operators.selector
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class EngineTest : FreeSpec({

    "An evolution [Engine]" - {
        "can be created" {
            checkAll(
                Arb.intGenotypeFactory(),
                Arb.positiveInt(),
                Arb.double(0.0..1.0),
                Arb.selector<Int, IntGene>(),
                Arb.selector<Int, IntGene>(),
                Arb.intAlterer(),
                Arb.limit(),
                Arb.selector<Int, IntGene>(),
                Arb.optimizer<Int, IntGene>(),
                Arb.evolutionListener<Int, IntGene>(),
                Arb.evaluator<Int, IntGene>()
            ) { genotype,
                populationSize,
                offspringRatio,
                selector,
                offspringSelector,
                alterer,
                limit,
                survivorSelector,
                optimizer,
                listener,
                evaluator ->
                val engine = Engine(
                    genotype,
                    populationSize,
                    offspringRatio,
                    selector,
                    offspringSelector,
                    alterer,
                    listOf(limit),
                    survivorSelector,
                    optimizer,
                    listOf(listener),
                    evaluator,
                    EvolutionInterceptor.identity()
                )
                engine.genotype shouldBe genotype
                engine.populationSize shouldBe populationSize
                engine.offspringRatio shouldBe offspringRatio
                engine.selector shouldBe selector
                engine.offspringSelector shouldBe offspringSelector
                engine.alterer shouldBe alterer
                engine.limits shouldBe listOf(limit)
                engine.survivorSelector shouldBe survivorSelector
                engine.optimizer shouldBe optimizer
                engine.listeners shouldBe listOf(listener)
                engine.evaluator shouldBe evaluator
            }
        }

        "have a [generation] property that" - {
            "starts at 0" {
                checkAll(Arb.engine()) { engine ->
                    engine.generation shouldBe 0
                }
            }
        }

        "have a "
    }
})
