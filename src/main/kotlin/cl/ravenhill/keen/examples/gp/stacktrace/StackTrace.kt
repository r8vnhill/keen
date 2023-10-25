/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.examples.gp.stacktrace

import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.chromosomes.AbstractChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

typealias Instruction = Pair<KFunction<*>, Map<KParameter, Any?>>

typealias MCR = MinimalCrashReproduction

data class MinimalCrashReproduction(val program: Individual<Instruction, InstructionGene>) {

    /// Documentation inherited from [Any].
    override fun toString() = program.genotype.toString()
}

class InstructionGene(override val dna: Instruction, private val tracer: Tracer<*>) :
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
        override fun make() = InstructionChromosome((0..<size).map { geneFactory() })
    }
}
