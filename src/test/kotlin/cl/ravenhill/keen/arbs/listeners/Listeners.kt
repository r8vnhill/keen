package cl.ravenhill.keen.arbs.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.EvolutionPlotter
import cl.ravenhill.keen.util.listeners.EvolutionPrinter
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.positiveInt

/**
 * Generates an arbitrary [EvolutionListener] for property-based testing.
 *
 * This function leverages the Kotest's property-based testing framework to create random instances
 * of different types of [EvolutionListener]. Currently, it can generate [EvolutionPlotter] instances.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the listener will be dealing with.
 * @return An arbitrary generator producing instances of [EvolutionListener].
 */
fun <T, G> Arb.Companion.evolutionListener() where G : Gene<T, G> =
    choice(evolutionPlotter<T, G>(), evolutionPrinter())

/**
 * Generates an arbitrary [EvolutionPlotter] for property-based testing.
 *
 * The [EvolutionPlotter] is a type of [EvolutionListener] that tracks and records the evolution process,
 * mainly for visualization or logging purposes. This function creates instances of [EvolutionPlotter]
 * with random configurations.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the plotter will be dealing with.
 * @return An arbitrary generator producing instances of [EvolutionPlotter].
 */
fun <T, G> Arb.Companion.evolutionPlotter() where G : Gene<T, G> = arbitrary { EvolutionPlotter<T, G>() }

/**
 * Generates an arbitrary [EvolutionPrinter] for property-based testing.
 *
 * The [EvolutionPrinter] is a specialized [EvolutionListener] that prints details about the evolution
 * process at specified intervals. This function creates instances of [EvolutionPrinter] with a random
 * frequency of printing generations.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the printer will be dealing with.
 * @return An arbitrary generator producing instances of [EvolutionPrinter].
 */
fun <T, G> Arb.Companion.evolutionPrinter() where G : Gene<T, G> =
    arbitrary { EvolutionPrinter<T, G>(positiveInt().bind()) }

