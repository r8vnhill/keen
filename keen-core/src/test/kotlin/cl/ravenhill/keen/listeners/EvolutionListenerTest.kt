/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.arb.anyRanker
import cl.ravenhill.keen.arb.evolution.evolutionState
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.individual
import cl.ravenhill.keen.arb.genetic.population
import cl.ravenhill.keen.arb.listeners.evolutionListener
import cl.ravenhill.keen.arb.listeners.evolutionRecord
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EvolutionListenerTest : FreeSpec({
    "An Evolution Listener" - {
        "should do nothing for all events" {
            val populationArb = Arb.population(Arb.individual(Arb.genotype(Arb.nothingChromosome())))
            checkAll(
                Arb.evolutionListener(Arb.anyRanker<Nothing, NothingGene>(), Arb.evolutionRecord()),
                Arb.evolutionState(populationArb, Arb.anyRanker())
            ) { listener, state ->
                listener.onEvolutionEnded(state) shouldBe Unit
                listener.onEvolutionStarted(state) shouldBe Unit
                listener.onGenerationEnded(state) shouldBe Unit
                listener.onGenerationStarted(state) shouldBe Unit
                listener.onAlterationEnded(state) shouldBe Unit
                listener.onAlterationStarted(state) shouldBe Unit
                listener.onEvaluationEnded(state) shouldBe Unit
                listener.onEvaluationStarted(state) shouldBe Unit
                listener.onInitializationEnded(state) shouldBe Unit
                listener.onInitializationStarted(state) shouldBe Unit
                listener.onParentSelectionEnded(state) shouldBe Unit
                listener.onParentSelectionStarted(state) shouldBe Unit
                listener.onSurvivorSelectionEnded(state) shouldBe Unit
                listener.onSurvivorSelectionStarted(state) shouldBe Unit
            }
        }

        "can be concatenated" {
            checkAll(
                Arb.evolutionListener(Arb.anyRanker<Nothing, NothingGene>(), Arb.evolutionRecord()),
                Arb.evolutionListener(Arb.anyRanker<Nothing, NothingGene>(), Arb.evolutionRecord()),
            ) { l1, l2 ->
                (l1 + l2) shouldBe listOf(l1, l2)
            }
        }
    }
})