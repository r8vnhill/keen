package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.utils.Box


/**
 * Applies a given block of code to a `GenerationRecord` contained within a `Box` if it is not null.
 *
 * ## Usage:
 * This function provides a convenient way to apply a block of code to a `GenerationRecord` within a `Box`.
 *
 * ### Example 1: Updating a GenerationRecord
 * ```
 * val generationBox = Box<GenerationRecord<Int, MyGene>?>(GenerationRecord(1))
 * mapGeneration(generationBox) {
 *     duration = 1000L
 * }
 * // The generation record's duration is now updated to 1000L
 * ```
 *
 * @param generation the `Box` containing the `GenerationRecord`
 * @param block the block of code to apply to the `GenerationRecord` if it is not null
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 */
fun <T, G> mapGeneration(
    generation: Box<GenerationRecord<T, G>?>,
    block: GenerationRecord<T, G>.() -> Unit
) where G : Gene<T, G> {
    generation.map { it?.apply(block) }
}
