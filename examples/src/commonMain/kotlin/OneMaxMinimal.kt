import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode
import cl.ravenhill.keen.dsl.booleans
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.geneticAlgorithm
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.BooleanGene
import cl.ravenhill.keen.limits.targetFitness
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter
import cl.ravenhill.keen.listeners.summary.EvolutionSummary
import cl.ravenhill.keen.operators.alteration.crossover.UniformCrossover
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator

/**
 * Counts the number of `true` values in a flattened genotype.
 *
 * @param genotype The genotype composed of Boolean genes.
 * @return The number of `true` values in the genotype as a Double.
 */
private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatten().count { it }.toDouble()

suspend fun oneMaxMinimal() {

    // Set the display mode for Boolean chromosomes to a simple binary string representation.
    Domain.toStringMode = ToStringMode.SIMPLE

    // Configure and initialize the genetic algorithm engine.
    val engine = geneticAlgorithm(
        ::count, // Fitness function: counts the number of true values in the genotype.
        genotypeOf {
            chromosomeOf {
                booleans {
                    size = 50 // Length of the binary string (genotype).
                    trueRate = 0.15 // Initial rate of true values (1-bits) in the binary string.
                }
            }
        }
    ) {
        alterers += listOf(BitFlipMutator(), UniformCrossover(chromosomeRate = 0.6)) // Mutation and crossover operators
        limits += targetFitness(50.0) // Evolution stops when fitness reaches 50.
        listeners += listOf(EvolutionSummary(), EvolutionPlotter()) // Listeners for evolution monitoring.
    }

    // Run the evolution process.
    engine.evolve()

    // Display the summary of the evolution.
    engine.publicListeners.filterIsInstance<EvolutionSummary<*, *, *, *>>().first().display()
    engine.publicListeners.filterIsInstance<EvolutionPlotter<*, *, *, *>>().first().display()
}
