/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.mutableList
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll


class EvolutionRecordTest : FreeSpec({
    "An [EvolutionRecord]" - {
        "can be created with a generation number" {
            checkAll(Arb.evolutionRecord()) { record ->
                record shouldBe EvolutionRecord(record.generations)
            }
        }
    }
})

private fun Arb.Companion.evolutionRecord() = arbitrary {
    EvolutionRecord<Any, AnyGene>(mutableList(generationRecord()).bind())
}

private class AnyGene: Gene<Any, AnyGene> {
    inner class IllegalOperationException(message: String): Exception(message)

    override val dna: Any
        get() = throw IllegalOperationException("This operation should not be called")

    override fun withDna(dna: Any): AnyGene {
        throw IllegalOperationException("This operation should not be called")
    }
}
