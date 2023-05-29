/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.problems.gp.stacktrace

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.limits.TargetFitness
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.operators.mutator.InversionMutator
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.mutator.SwapMutator
import cl.ravenhill.keen.util.statistics.StatisticCollector
import cl.ravenhill.keen.util.statistics.StatisticPrinter
import cl.ravenhill.utils.runWithStdoutOff
import java.lang.ArithmeticException
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

private typealias Statement = Pair<KFunction<*>, Map<KParameter, Any>>

class Tracer<T : Throwable>(
    val statements: List<KFunction<*>>,
    val targetException: KClass<T>,
    val targetMessage: String = "",
    val targetFunction: String = ""
) {
    private val engine = engine(::fitness, genotype {
        chromosome {
            StatementChromosome.Factory(5) {
                StatementGene(generateInstruction())
            }
        }
    }) {
        populationSize = 2
        alterers = listOf(SwapMutator(0.5), Mutator(0.1), SinglePointCrossover(0.5))
        limits = listOf(TargetFitness(5.0))
        statistics = listOf(StatisticCollector(), StatisticPrinter(20))
    }
    private val inputFactory = InputFactory()

    fun run() {
        val result = engine.evolve()
        println(engine.statistics.first())
    }

    fun fitness(
        genotype: Genotype<Statement, StatementGene>
    ): Double {
        var fitness = 0.0
        lateinit var stack: Array<StackTraceElement>
        val statements = genotype.chromosomes.first()
        try {
            runWithStdoutOff {
                statements.forEach { it() }
            }
            return 0.0
        } catch (invocationException: InvocationTargetException) {
            val ex = invocationException.targetException
            stack = ex.stackTrace
            if (targetException == ex::class) {
                fitness += 2
                if (targetMessage.isEmpty() || targetMessage in (ex.message ?: "")) {
                    fitness++
                }
            }
        }
        if (targetFunction.isEmpty() || stack.any { it.methodName == targetFunction }) {
            fitness += 2
        }
        return fitness
    }

    private fun generateInstruction(): Statement {
        val instruction = statements.random(Core.random)
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

    fun execute(statements: List<Statement>) {
        val results = mutableListOf<Any?>()
        statements.forEach { (function, params) ->
            results.add(function.callBy(params))
        }
    }
}

class StatementGene(override val dna: Statement) : Gene<Statement, StatementGene> {

    operator fun invoke() = dna.first.callBy(dna.second)

    override fun withDna(dna: Statement) = StatementGene(dna)

    override fun toString() =
        dna.first.name + dna.second.values.joinToString(", ", "(", ")")
}


class StatementChromosome(override val genes: List<StatementGene>) :
    Chromosome<Statement, StatementGene> {

    override fun withGenes(genes: List<StatementGene>) = StatementChromosome(genes)

    override fun toString() = genes.joinToString("\n")

    class Factory(override var size: Int, val geneFactory: () -> StatementGene) :
        Chromosome.AbstractFactory<Statement, StatementGene>() {
        @OptIn(ExperimentalStdlibApi::class)
        override fun make() = StatementChromosome((0..<size).map { geneFactory() })
    }
}

fun main() {
//    val tracer1 = Tracer.create<NullPointerException>(functions0)
//    tracer1.run()
//    val tracer2 = Tracer.create<ArithmeticException>(functions0)
//    tracer2.run()
    val tracer3 = Tracer.create<IllegalArgumentException>(functions0, "Input string must not be blank.")
    tracer3.run()
}