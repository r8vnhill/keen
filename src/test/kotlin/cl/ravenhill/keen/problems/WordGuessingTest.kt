package cl.ravenhill.keen.problems

import cl.ravenhill.keen.Builders
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.selector.RouletteWheelSelector
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.ascii
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class WordGuessingTest : FreeSpec({
    fun matches(target: String) = { genotype: Genotype<Char> ->
        genotype.flatten()
            .filterIndexed { index, char -> char == target[index] }
            .size.toDouble()
    }

    "!The Evolution Engine should be able to guess a word" {
        checkAll(
            PropTestConfig(iterations = 50),
            Arb.string(1..20, Codepoint.ascii())
        ) { target ->
            val engine = Builders.engine(matches(target), Builders.genotype {
                chromosome { Builders.Chromosomes.chars { size = target.length } }
            }) {
                populationSize = 500
                survivorSelector = RouletteWheelSelector()
                alterers = listOf(Mutator(0.03), SinglePointCrossover(0.2))
                limits = listOf(TargetFitness(target.length.toDouble()))
            }
            val evolvedPopulation = engine.run()
            evolvedPopulation.best!!.fitness shouldBe target.length.toDouble()
            evolvedPopulation.best!!.genotype.flatten().joinToString("") shouldBe target
        }
    }
})