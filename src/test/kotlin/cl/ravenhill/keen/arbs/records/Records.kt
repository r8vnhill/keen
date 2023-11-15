/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.records

import cl.ravenhill.keen.arbs.datatypes.list
import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.arbs.genetic.genotype
import cl.ravenhill.keen.arbs.genetic.intGenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import kotlin.time.TimeSource


/**
 * Provides an arbitrary (Arb) instance that generates a [GenerationRecordData] object.
 *
 * The provided Arb object generates a GenerationRecordData object, which contains a non-negative
 * integer representing the generation number. This is useful when you need an arbitrary
 * generation record for testing or other purposes.
 *
 * @return An Arb instance that generates a [GenerationRecordData] object.
 */
fun Arb.Companion.generationRecord(size: Arb<Int> = int(1..50)) = arbitrary {
    GenerationRecord<Int, IntGene>(nonNegativeInt().bind()).apply {
        startTime = TimeSource.Monotonic.markNow()
        population.resulting = list(individualRecord(), size).bind()
    }
}

/**
 * Produces an arbitrary generator for [IndividualRecord] suitable for property-based testing.
 *
 * This extension function for [Arb.Companion] allows the generation of random `IndividualRecord` instances,
 * facilitating the use of property-based tests with Kotest's [Arb] API.
 *
 * Each generated `IndividualRecord` has the following attributes:
 * 1. A genotype, which is transformed into a simple string representation.
 * 2. A random double value.
 *
 * It's assumed that the `genotype()` function provides the necessary genotype data for the record.
 *
 * @return An arbitrary generator that yields [IndividualRecord] instances with randomized data.
 */
fun Arb.Companion.individualRecord() = arbitrary {
    IndividualRecord(intGenotype().bind(), Arb.real().bind())
}


/**
 * Produces an arbitrary generator for [GenerationRecord.PopulationRecord] suitable for property-based testing.
 *
 * This extension function for [Arb.Companion] facilitates the generation of random `PopulationRecord` instances,
 * which belong to the `GenerationRecord` class, for use in property-based tests. It utilizes the [Arb] API from
 * Kotest's property-based testing framework.
 *
 * Each generated `PopulationRecord` contains:
 * 1. A list of `IndividualRecord` instances.
 *
 * The method leverages the `individualRecord()` function to generate individual records for the population.
 *
 * @return An arbitrary generator that creates [GenerationRecord.PopulationRecord] instances with randomized data.
 */
fun Arb.Companion.populationRecord() = arbitrary {
    GenerationRecord.PopulationRecord<Int, IntGene>().apply {
        resulting = list(individualRecord()).bind()
    }
}
