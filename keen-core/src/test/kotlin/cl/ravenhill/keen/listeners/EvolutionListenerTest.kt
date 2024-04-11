/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.anyRanker
import cl.ravenhill.keen.arb.evolution.arbEvolutionState
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.listeners.arbEvolutionListener
import cl.ravenhill.keen.arb.listeners.arbEvolutionRecord
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EvolutionListenerTest : FreeSpec({
    "An Evolution Listener" - {
        "should do nothing for all events" {
            val populationArb = arbPopulation(arbIndividual(Arb.genotype(arbNothingChromosome())))
            checkAll(
                arbEvolutionListener(KeenArb.anyRanker<Nothing, NothingGene>(), arbEvolutionRecord()),
                arbEvolutionState(populationArb, KeenArb.anyRanker())
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
                arbEvolutionListener(KeenArb.anyRanker<Nothing, NothingGene>(), arbEvolutionRecord()),
                arbEvolutionListener(KeenArb.anyRanker<Nothing, NothingGene>(), arbEvolutionRecord()),
            ) { l1, l2 ->
                (l1 + l2) shouldBe listOf(l1, l2)
            }
        }
    }
})