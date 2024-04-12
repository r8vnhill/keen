package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.operators.arbBaseCrossover
import cl.ravenhill.keen.assertions.`crossover should return a list of offspring`
import cl.ravenhill.keen.assertions.`crossover should throw an exception on incorrect configuration`
import cl.ravenhill.keen.assertions.genericCrossover
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map

class CrossoverTest : FreeSpec({
    "Crossing a list of parents" - {
        `crossover should return a list of offspring`()
        `crossover should throw an exception on incorrect configuration`()
    }
})

private fun nonEmptyChromosome(size: Int): Arb<IntChromosome> =
    arbIntChromosome(Arb.int(size..size)).filter { it.size > 0 }

private fun genotypeList(size: Int): Arb<List<Genotype<Int, IntGene>>> =
    Arb.list(arbGenotype(nonEmptyChromosome(5), Arb.constant(5)), size..size)

private fun crossoverAndParents(): Arb<Pair<Crossover<Int, IntGene>, List<Genotype<Int, IntGene>>>> =
    arbBaseCrossover<Int, IntGene>().flatMap { crossover ->
        genotypeList(crossover.numParents).map { parents -> crossover to parents }
    }

