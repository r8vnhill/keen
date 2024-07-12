/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.stacktrace

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.limits.MaxGenerations
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.operators.selection.TournamentSelector
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class Tracer<T : Throwable>(
    private val functions: List<KFunction<*>>,
    private val targetException: KClass<T>,
    private val targetMessage: String,
    private val targetFunctions: List<String>,
    private val mutator: Mutator<KFunction<*>, KFunctionGene>,
    private val crossover: Crossover<KFunction<*>, KFunctionGene>,
    private val listeners: List<EvolutionListener<KFunction<*>, KFunctionGene>>,
) {
    private val engine = evolutionEngine(
        ::fitnessFunction,
        genotypeOf {
            chromosomeOf {
                KFunctionChromosome.Factory(10) {
                    KFunctionGene(functions.random(Domain.random), functions)
                }
            }
        }
    ) {
        this.populationSize = 1000
        this.parentSelector = TournamentSelector()
        this.survivorSelector = TournamentSelector()
        this.alterers += mutator + crossover
        this.limits += listOf(TargetFitness(5.0), MaxGenerations(1000))
        this.listeners += this@Tracer.listeners
    }

    fun run(): MinimalCrashReproduction {
        val result = engine.evolve()
        val program = minimize(result).population
            .groupBy { it.fitness }
            .maxByOrNull { it.key }?.value
            ?.minByOrNull { it.flatten().size }
        return MinimalCrashReproduction(program!!)
    }

    private fun minimize(result: EvolutionState<KFunction<*>, KFunctionGene>) = result.trimStart().trimEnd()

    private fun EvolutionState<KFunction<*>, KFunctionGene>.trimEnd() = map {
        // Iteratively remove the last instruction from the genotype
        for (i in 1..it.genotype.first().size) {
            val candidate = Genotype(KFunctionChromosome(it.genotype.first().take(i)))
            val candidateFitness = fitnessFunction(candidate)
            // If fitness is maintained or improved, replace the genotype with the new minimized one
            if (candidateFitness >= it.fitness) {
                return@map it.copy(genotype = candidate, fitness = candidateFitness)
            }
        }
        it
    }


    private fun EvolutionState<KFunction<*>, KFunctionGene>.trimStart() = map {
        // Iteratively remove the first instruction from the genotype
        for (i in it.genotype.first().size downTo 1) {
            val candidate = Genotype(KFunctionChromosome(it.genotype.first().drop(i)))
            val candidateFitness = fitnessFunction(candidate)
            // If fitness is maintained or improved, replace the genotype with the new minimized one
            if (candidateFitness >= it.fitness) {
                return@map it.copy(genotype = candidate, fitness = candidateFitness)
            }
        }
        it
    }

    private fun fitnessFunction(genotype: Genotype<KFunction<*>, KFunctionGene>) = genotype.first().let { statements ->
        try {
            val variables = mutableListOf<Any?>()
            statements.forEach {
                variables += it(variables.takeLast(it.arity))
            }
            0.0
        } catch (invocationTargetException: InvocationTargetException) {
            var fitness = 0.0
            val ex = invocationTargetException.targetException
            val stack = ex.stackTrace
            if (targetException == ex::class) {
                fitness += 2.0
                if (targetMessage.isEmpty() || targetMessage == ex.message) {
                    fitness++
                }
            }
            if (targetFunctions.isEmpty() || stack.any { it.methodName in targetFunctions }) {
                fitness += 2 * if (targetFunctions.isEmpty()) {
                    1.0
                } else {
                    stack.count { it.methodName in targetFunctions }.toDouble() / targetFunctions.size
                }
            }
            fitness
        } catch (e: Exception) {
            0.0
        }
    }

    companion object {
        inline fun <reified E : Throwable> create(
            functions: List<KFunction<*>>,
            targetMessage: String = "",
            targetFunction: List<String> = emptyList(),
            mutator: Mutator<KFunction<*>, KFunctionGene> = RandomMutator(individualRate = 0.2),
            listeners: List<EvolutionListener<KFunction<*>, KFunctionGene>> = emptyList(),
            crossover: Crossover<KFunction<*>, KFunctionGene>,
        ) = Tracer(functions, E::class, targetMessage, targetFunction, mutator, crossover, listeners)
    }
}
