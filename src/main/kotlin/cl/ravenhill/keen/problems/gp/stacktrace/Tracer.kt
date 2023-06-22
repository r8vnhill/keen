/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.problems.gp.stacktrace

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.listeners.EvolutionListener
import cl.ravenhill.keen.util.listeners.Listeners
import cl.ravenhill.utils.runWithStdoutOff
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/**
 * `Tracer` is a class that uses genetic programming to find the minimal crash reproduction
 * of a given exception.
 *
 * @param functions A list of functions for evolution.
 * @param targetException The target exception class for fitness evaluation.
 * @param targetMessage The target message for fitness evaluation.
 * @param targetFunction The target function name for fitness evaluation.
 * @param statCollectors A list of statistic collectors for evolution.
 *
 * @property engine The genetic programming engine.
 * @property inputFactory The input factory.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Tracer<T : Throwable>(
    val functions: List<KFunction<*>>,
    private val targetException: KClass<T>,
    private val targetMessage: String,
    private val targetFunction: String,
    private val populationSize: Int,
    private val statCollectors: List<EvolutionListener<Instruction, InstructionGene>>
) {
    val engine = engine(::fitness, genotype {
        chromosome {
            InstructionChromosome.Factory(5) {
                InstructionGene(generateInstruction(), this@Tracer)
            }
        }
    }) {
        populationSize = this@Tracer.populationSize
        alterers = listOf(Mutator(0.3), SinglePointCrossover(0.5))
        limits = listOf(TargetFitness(5.0))
        statistics = this@Tracer.statCollectors
    }
    val inputFactory = InputFactory()

    fun run(): MinimalCrashReproduction {
        val result = engine.evolve()
        val program = minimize(result).population
            .groupBy { it.fitness }
            .maxBy { it.key }.value
            .minBy { it.flatten().size }
        return MinimalCrashReproduction(program)
    }

    fun fitness(genotype: Genotype<Instruction, InstructionGene>) =
        genotype.chromosomes.first().let { instructions ->
            try {
                runWithStdoutOff {
                    instructions.forEach { it() }
                }
                0.0
            } catch (invocationException: InvocationTargetException) {
                var fitness = 0.0
                val ex = invocationException.targetException
                val stack = ex.stackTrace
                if (targetException == ex::class) {
                    fitness += 2
                    if (targetMessage.isEmpty() || targetMessage in (ex.message ?: "")) {
                        fitness++
                    }
                }
                if (targetFunction.isEmpty() || stack.any { it.methodName == targetFunction }) {
                    fitness += 2
                }
                fitness
            }
        }

    fun generateInstruction(): Instruction {
        val instruction = functions.random(Core.random)
        val params = mutableMapOf<KParameter, Any?>()
        instruction.parameters.forEach { param ->
            params[param] = inputFactory[param.type].invoke()
        }
        return instruction to params
    }

    /**
     * This method aims to minimize the genotype size while maintaining or improving its fitness.
     * It performs two stages of minimization on the evolution result's genotypes.
     *
     * 1. In the first stage, it iteratively removes the last instruction from the genotype and
     * checks if the fitness of the genotype either remains the same or improves. If it does,
     * it replaces the old genotype with the new minimized one. This is done from the start of
     * the genotype until no more instructions can be removed without degrading fitness.
     *
     * 2. In the second stage, it does a similar process, but this time it removes instructions
     * from the start of the genotype. Again, this is done until no more instructions can be
     * removed without degrading fitness.
     *
     * @param result The [EvolutionResult] whose genotype we want to minimize.
     * @return An [EvolutionResult] with minimized genotype without degrading fitness.
     */
    private fun minimize(
        result: EvolutionResult<Instruction, InstructionGene>
    ): EvolutionResult<Instruction, InstructionGene> = result.trimEnd().trimStart()

    // region : -== TRIMMING METHODS (CODE DUPLICATION ON PURPOSE) ==-
    /**
     * This extension function performs a genotype size minimization on the end (last elements)
     * of the genotype within the given [EvolutionResult].
     *
     * @return An [EvolutionResult] where the genotype has been trimmed from the end while
     * maintaining or improving fitness.
     */
    private fun EvolutionResult<Instruction, InstructionGene>.trimEnd() = map {
        // Iteratively remove the last instruction from the genotype
        for (i in 1..it.genotype.first().size) {
            val candidate = Genotype(InstructionChromosome(it.genotype.first().take(i)))
            val candidateFitness = fitness(candidate)
            // If fitness is maintained or improved, replace the genotype with the new minimized one
            if (candidateFitness >= it.fitness) {
                return@map it.withGenotype(candidate, candidateFitness)
            }
        }
        it
    }

    /**
     * This extension function performs a genotype size minimization on the start (first elements)
     * of the genotype within the given [EvolutionResult].
     *
     * @return An [EvolutionResult] where the genotype has been trimmed from the start while
     * maintaining or improving fitness.
     */
    private fun EvolutionResult<Instruction, InstructionGene>.trimStart() = map {
        // Iteratively remove the first instruction from the genotype
        for (i in it.genotype.first().size downTo 1) {
            val candidate = Genotype(InstructionChromosome(it.genotype.first().drop(i)))
            val candidateFitness = fitness(candidate)
            // If fitness is maintained or improved, replace the genotype with the new minimized one
            if (candidateFitness >= it.fitness) {
                return@map it.withGenotype(candidate, candidateFitness)
            }
        }
        it
    }
    // endregion TRIMMING METHODS (CODE DUPLICATION ON PURPOSE)

    /**
     * Companion object for [Tracer].
     */
    companion object {
        /**
         * Factory function for creating a [Tracer] instance with a specific exception type.
         *
         * [Tracer] is a tool for finding the minimal crash reproduction of a given exception.
         * It handles exception stack traces, searching for a specific message and function, and
         * storing statistics.
         *
         * ## Examples
         * ### Example 1: Creating a Tracer for IllegalArgumentException
         * ```
         * val functions = listOf(::checkPositiveNumber, ::checkNonNullString, ::checkNonZeroDivisor)
         * val tracer = create<IllegalArgumentException>(functions, "Input number must be positive.")
         * ```
         *
         * @param functions A list of [KFunction] references which the [Tracer] can use to generate
         * individuals.
         * @param targetMessage The specific exception message that the [Tracer] will try to
         * produce.
         * Defaults to an empty string.
         * @param functionName The specific function name where the exception is expected to be
         * thrown.
         * Defaults to an empty string.
         * @param populationSize The size of the population for each generation of the genetic
         * programming problem.
         * Defaults to 4.
         * @param statCollectors A list of [EvolutionListener] instances for collecting statistics
         * over the course of the genetic algorithm's execution.
         * Defaults to an empty list.
         * @return A new [Tracer] instance parameterized with the specified [E] exception type.
         *
         * @param E The reified type of the [Throwable] (exception) that the [Tracer] will handle.
         */
        inline fun <reified E : Throwable> create(
            functions: List<KFunction<*>>,
            targetMessage: String = "",
            functionName: String = "",
            populationSize: Int = 4,
            statCollectors: Listeners<Instruction, InstructionGene> = emptyList()
        ) = Tracer(functions, E::class, targetMessage, functionName, populationSize, statCollectors)
    }
}
