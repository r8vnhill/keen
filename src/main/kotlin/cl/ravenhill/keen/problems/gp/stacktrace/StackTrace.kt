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
        limits = listOf(TargetFitness(5.0))
    }
    private val inputFactory = InputFactory()

    fun run() {

    }

    fun fitness(
        statements: Genotype<Statement, StatementGene>
    ): Double {
        var fitness = 0.0
        lateinit var stack: Array<StackTraceElement>
        try {
            statements.forEach { it[0]() }
            return 0.0
        } catch (e: Throwable) {
            stack = e.stackTrace
            if (targetException == e::class) {
                fitness += 2
                if (targetMessage.isEmpty() || targetMessage in (e.message ?: "")) {
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
        inline fun <reified T, reified E : Throwable> create(
            targetMessage: String = "",
            functionName: String = ""
        ) = Tracer(
            T::class.members.filterIsInstance<KFunction<*>>(),
            E::class,
            targetMessage,
            functionName
        )
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

    override fun withDna(dna: Statement): StatementGene {
        TODO("Not yet implemented")
    }
}


class StatementChromosome(override val genes: List<StatementGene>) :
        Chromosome<Statement, StatementGene> {

    override fun withGenes(genes: List<StatementGene>): Chromosome<Statement, StatementGene> {
        TODO("Not yet implemented")
    }

    class Factory(override var size: Int, val geneFactory: () -> StatementGene) :
            Chromosome.AbstractFactory<Statement, StatementGene>() {
        @OptIn(ExperimentalStdlibApi::class)
        override fun make() = StatementChromosome((0..<size).map { geneFactory() })
    }
}
fun main() {
    val tracer = Tracer.create<Tracer<Throwable>, Throwable>()

}