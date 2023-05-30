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
import cl.ravenhill.keen.util.statistics.StatisticCollector
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
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class Tracer<T : Throwable>(
    val functions: List<KFunction<*>>,
    private val targetException: KClass<T>,
    private val targetMessage: String,
    private val targetFunction: String,
    private val statCollectors: List<StatisticCollector<Instruction, InstructionGene>>
) {
    val engine = engine(::fitness, genotype {
        chromosome {
            InstructionChromosome.Factory(5) {
                InstructionGene(generateInstruction(), this@Tracer)
            }
        }
    }) {
        populationSize = 4
        alterers = listOf(Mutator(0.3), SinglePointCrossover(0.5))
        limits = listOf(TargetFitness(5.0))
        statistics = this@Tracer.statCollectors
    }
    private val inputFactory = InputFactory()

    fun run(): MinimalCrashReproduction {
        val result = engine.evolve()
        val program = minimize(result).population
            .groupBy { it.fitness }
            .maxBy { it.key }.value
            .minBy { it.flatten().size }
        println(program)
        return MinimalCrashReproduction(program)
    }

    private fun minimize(
        result: EvolutionResult<Instruction, InstructionGene>
    ): EvolutionResult<Instruction, InstructionGene> = result.map {
        for (i in 1..it.genotype.first().size) {
            val candidate = Genotype(InstructionChromosome(it.genotype.first().take(i)))
            val candidateFitness = fitness(candidate)
            if (candidateFitness >= it.fitness) {
                return@map it.withGenotype(candidate, candidateFitness)
            }
        }
        it
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

    companion object {
        inline fun <reified E : Throwable> create(
            functions: List<KFunction<*>>,
            targetMessage: String = "",
            functionName: String = "",
            statCollectors: List<StatisticCollector<Instruction, InstructionGene>> = emptyList()
        ) = Tracer(functions, E::class, targetMessage, functionName, statCollectors)
    }
}