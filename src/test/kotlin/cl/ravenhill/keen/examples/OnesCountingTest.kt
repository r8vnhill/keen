package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Builders
import cl.ravenhill.keen.Builders.Chromosomes.booleans
import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.TournamentSelector
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.ints


class OnesCountingTest : FreeSpec({
    fun count(genotype: Genotype<Boolean>) = genotype.flatten().count { it }.toDouble()
    lateinit var engine: Engine<Boolean>

    "The Evolution Engine should be able to solve the ones counting problem" {
        checkAll(Exhaustive.ints(1..50)) { _ ->
            engine = Builders.engine(::count, Builders.genotype {
                chromosome {
                    booleans { size = 20; truesProbability = 0.15 }
                }
            }) {
                populationSize = 500
                selector = TournamentSelector(2)
                alterers = listOf(Mutator(0.03), SinglePointCrossover(0.2))
                limits = listOf(GenerationCount(100), TargetFitness(20.0))
            }
            val result = engine.run()
            result.best!!.fitness shouldBe 20.0
            result.generation shouldBeLessThan 100
        }
    }
})