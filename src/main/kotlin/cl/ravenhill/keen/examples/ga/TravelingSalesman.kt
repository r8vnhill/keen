package cl.ravenhill.keen.examples.ga

import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.operators.crossover.permutation.PartiallyMappedCrossover
import cl.ravenhill.keen.operators.mutator.InversionMutator
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.plotly.Plot
import tech.tablesaw.plotly.components.Figure
import tech.tablesaw.plotly.components.Layout
import tech.tablesaw.plotly.components.Marker
import tech.tablesaw.plotly.traces.ScatterTrace
import java.util.Objects
import kotlin.math.pow
import kotlin.math.sqrt

private fun fitnessFn(genotype: Genotype<Pair<Int, Int>>): Double {
    val routePoints = genotype.flatten()
    var distance = 0.0
    for (i in 1 until points.size) {
        distance += routePoints[i] distanceTo routePoints[i - 1]
    }
    return distance
}

private infix fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>) = sqrt(
    (this.first - other.first).toDouble().pow(2)
            + (this.second - other.second).toDouble().pow(2)
)

class RoutePointGene(override val dna: Pair<Int, Int>) : Gene<Pair<Int, Int>> {
    override fun mutate() = TODO()

    override fun generator(): Pair<Int, Int> {
        TODO("Not yet implemented")
    }

    override fun duplicate(dna: Pair<Int, Int>) = RoutePointGene(dna)
    override fun toString() = "(${dna.first}, ${dna.second})"
    override fun equals(other: Any?) = other is RoutePointGene && dna == other.dna
    override fun hashCode() = Objects.hash(RoutePointGene::class, dna)
}

class RouteChromosome(override val genes: List<Gene<Pair<Int, Int>>>) :
        Chromosome<Pair<Int, Int>> {

    override fun duplicate(genes: List<Gene<Pair<Int, Int>>>) =
        RouteChromosome(genes.map { RoutePointGene(it.dna) })

    override fun toString() = genes.joinToString(" -> ")

    class Factory : Chromosome.Factory<Pair<Int, Int>> {
        override fun make() =
            RouteChromosome(points.shuffled(Core.random).map { RoutePointGene(it) })
    }
}

fun main() {
    val engine = engine(::fitnessFn, genotype {
        chromosome { RouteChromosome.Factory() }
    }) {
        populationSize = 1000
        limits = listOf(GenerationCount(200))
        alterers = listOf(InversionMutator(0.1), PartiallyMappedCrossover(0.3))
        optimizer = FitnessMinimizer()
        statistics = listOf(StatisticCollector(), StatisticPrinter(30), StatisticPlotter())
    }
    val result = engine.run()
    println(engine.statistics.first())
    println(result)
    (engine.statistics.last() as StatisticPlotter).displayFitness()
    val x = DoubleColumn.create("x",
        result.best?.genotype?.flatten()?.map { it.first.toDouble() } ?: listOf())
    val y = DoubleColumn.create(
        "y",
        result.best?.genotype?.flatten()?.map { it.second.toDouble() }
            ?: listOf())
    val scatterX = ScatterTrace.builder(x, y)
        .mode(ScatterTrace.Mode.LINE)
        .marker(
            Marker.builder()
                .size(10.0)
                .build()
        )
        .build()
    Plot.show(
        Figure.builder()
            .addTraces(scatterX)
            .layout(Layout.builder("Best Route").build())
            .build()
    )
}

val points = listOf(
    100 to 160,
    20 to 40,
    60 to 20,
    180 to 100,
    200 to 40,
    60 to 200,
    80 to 180,
    40 to 120,
    140 to 180,
    140 to 140,
    20 to 160,
    200 to 160,
    180 to 60,
    100 to 120,
    120 to 80,
    100 to 40,
    20 to 20,
    60 to 80,
    180 to 200,
    160 to 20
)