/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.problems.gp.stacktrace

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.evolution.EvolutionInterceptor
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPlotter
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import cl.ravenhill.utils.runWithStdoutOff
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

private typealias Instruction = Pair<KFunction<*>, Map<KParameter, Any?>>

class Tracer<T : Throwable>(
    val functions: List<KFunction<*>>,
    val targetException: KClass<T>,
    val targetMessage: String = "",
    val targetFunction: String = ""
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
        statistics = listOf(StatisticCollector(), StatisticPrinter(10), StatisticPlotter())
//        interceptor = EvolutionInterceptor.after { evResult ->
//            minimize(evResult)
//        }
    }
    private val inputFactory = InputFactory()

    fun run() {
        val result = engine.evolve()
        val program = minimize(result).population
            .groupBy { it.fitness }
            .maxBy { it.key }.value
            .minBy { it.flatten().size }
        println(program)
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

    fun fitness(
        genotype: Genotype<Instruction, InstructionGene>
    ): Double {
        var fitness = 0.0
        lateinit var stack: Array<StackTraceElement>
        val statements = genotype.chromosomes.first()
        return try {
            runWithStdoutOff {
                statements.forEach { it() }
            }
            0.0
        } catch (invocationException: InvocationTargetException) {
            val ex = invocationException.targetException
            stack = ex.stackTrace
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
        val params = mutableMapOf<KParameter, Any>()
        instruction.parameters.forEach { param ->
            params[param] = inputFactory.random(param.type)
        }
        return instruction to params
    }

    companion object {
        inline fun <reified E : Throwable> create(
            functions: List<KFunction<*>>,
            targetMessage: String = "",
            functionName: String = ""
        ) = Tracer(functions, E::class, targetMessage, functionName)
    }

    fun execute(statements: List<Instruction>) {
        val results = mutableListOf<Any?>()
        statements.forEach { (function, params) ->
            results.add(function.callBy(params))
        }
    }
}

class InstructionGene(override val dna: Instruction, val tracer: Tracer<*>) :
        Gene<Instruction, InstructionGene> {

    operator fun invoke() = dna.first.callBy(dna.second)

    override fun withDna(dna: Instruction) = InstructionGene(dna, tracer)

    override fun generator() = tracer.generateInstruction()

    override fun toString() =
        dna.first.name + dna.second.values.joinToString(", ", "(", ")")
}


class InstructionChromosome(override val genes: List<InstructionGene>) :
        AbstractChromosome<Instruction, InstructionGene>(genes) {

    override fun withGenes(genes: List<InstructionGene>) = InstructionChromosome(genes)

    override fun toString() = genes.joinToString("\n")

    class Factory(override var size: Int, val geneFactory: () -> InstructionGene) :
            Chromosome.AbstractFactory<Instruction, InstructionGene>() {
        @OptIn(ExperimentalStdlibApi::class)
        override fun make() = InstructionChromosome((0..<size).map { geneFactory() })
    }
}

fun main() {
//    val tracer1 = Tracer.create<NullPointerException>(functions0)
//    tracer1.run()
//    val tracer2 = Tracer.create<ArithmeticException>(functions0)
//    tracer2.run()
//    val tracer3 = Tracer.create<IllegalArgumentException>(functions0, "Input string must not be blank.")
//    tracer3.run()
    val tracer4 =
        Tracer.create<IllegalArgumentException>(functions0, functionName = "throwException1")
    tracer4.run()
    (tracer4.engine.statistics.last() as StatisticPlotter).displayFitness()
}