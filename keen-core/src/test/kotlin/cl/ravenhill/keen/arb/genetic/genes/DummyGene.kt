/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arb.genetic.genes

import cl.ravenhill.jakt.utils.DoubleRange
import cl.ravenhill.keen.arb.datatypes.orderedPair
import cl.ravenhill.keen.arb.range
import cl.ravenhill.keen.genetic.genes.BooleanGene
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next

/**
 * A dummy implementation of the [Gene] interface for testing purposes.
 *
 * This class represents a simple gene that encapsulates an integer value. It is designed for
 * use in unit tests or simulations where complex genetic behavior is not required.
 *
 * @property value The integer value encapsulated by this gene.
 * @property isValid A boolean indicating whether this gene is considered valid.
 */
data class DummyGene(
    override val value: Int,
    val isValid: Boolean,
) : Gene<Int, DummyGene> {

    /**
     * Creates a new [DummyGene] with the specified value.
     *
     * This method allows for the creation of a new gene instance with a different value
     * while preserving the validity state of the original gene.
     *
     * @param value The new value for the gene.
     * @return A new [DummyGene] instance with the given value and the same validity state.
     */
    override fun duplicateWithValue(value: Int): DummyGene = DummyGene(value, isValid)

    /**
     * Verifies the validity of this gene.
     *
     * @return [isValid] property, indicating whether this gene is considered valid.
     */
    override fun verify(): Boolean = isValid
}

/**
 * Generates an arbitrary [DummyGene] for property-based testing.
 *
 * This function creates instances of [DummyGene] with random integer values and validity states.
 * It is useful for testing genetic algorithms where gene behavior needs to be simulated
 * without complex logic.
 *
 * @param value An [Arb]<[Int]> generator for the integer value of the gene.
 * @param isValid An [Arb]<[Boolean]> generator for the validity state of the gene.
 *
 * @return An [Arb] that generates [DummyGene] instances with randomized value and validity.
 */
fun Arb.Companion.gene(value: Arb<Int> = int(), isValid: Arb<Boolean> = boolean()) = arbitrary {
    DummyGene(value.bind(), isValid.bind())
}

/**
 * Creates an arbitrary generator for `BooleanGene`.
 *
 * This function is an extension to the `Arb.Companion` object and is used to generate instances of `BooleanGene`,
 * which is a simple representation of a gene with boolean values. It provides a convenient way to create genes
 * that can either be `True` or `False`, mirroring the behavior of binary genetic traits.
 *
 * ## Usage:
 * This generator is particularly useful in scenarios involving genetic algorithms or simulations where binary
 * genetic traits are being modeled. It allows for the creation of genes that are either `True` or `False` based
 * on the provided arbitrary.
 *
 * ### Example:
 * ```kotlin
 * val booleanGeneGen = Arb.booleanGene()
 * val booleanGene = booleanGeneGen.bind() // Generates either BooleanGene.True or BooleanGene.False
 * ```
 *
 * @param value`: An `Arb<Boolean>` which provides the source of boolean values. By default, it uses a standard boolean
 *   arbitrary that generates `true` or `false` values with equal probability.
 * @return An `Arb<BooleanGene>`, capable of generating `BooleanGene` instances based on the provided boolean values.
 */
fun Arb.Companion.booleanGene(value: Arb<Boolean> = boolean()) = arbitrary {
    if (value.bind()) BooleanGene.True else BooleanGene.False
}

fun Arb.Companion.charGene(
    value: Arb<Char> = char(),
    range: Arb<ClosedRange<Char>> = range(char(), char()),
    filter: (Char) -> Boolean = { true },
) = arbitrary {
    CharGene(value.bind(), range.bind(), filter)
}

/**
 * Generates an arbitrary instance of [DoubleGene] for property-based testing in genetic algorithms.
 *
 * This function creates [DoubleGene] instances with configurable value ranges and optional custom filtering.
 * It is particularly useful for testing scenarios involving genetic algorithms with floating-point data.
 *
 * ## Functionality:
 * - The [range] parameter allows specifying the range within which the gene's value should fall. It uses a pair
 *   of doubles and ensures that the lower bound is less than the upper bound.
 * - The [value] parameter determines the actual value of the gene. It is generated within the specified range.
 * - The [filter] function provides an additional layer of customization, enabling the specification of constraints
 *   or conditions that the gene's value must meet.
 *
 * ## Usage:
 * ### Generating a [DoubleGene] with a specific range and custom filter:
 * ```kotlin
 * val doubleGeneArb = Arb.doubleGene(
 *     range = Arb.doubleRange(0.0..10.0), // Specifying the range
 *     filter = { it % 2 == 0.0 }           // Custom filter for even values
 * )
 * val doubleGene = doubleGeneArb.bind()   // Resulting DoubleGene will have an even value between 0.0 and 10.0
 * ```
 *
 * This generator is useful in scenarios where genes representing floating-point values are required to have
 * specific characteristics, such as falling within a certain range or satisfying certain conditions.
 *
 * @param range An [Arb] of [DoubleRange] representing the range within which the gene's value should fall.
 *              Defaults to a range generated from two double values, ensuring the lower bound is less than
 *              the upper bound.
 * @param value An [Arb] of [Double] for generating the gene's value within the specified range. Defaults to
 *              generating a random double within the provided range.
 * @param filter A lambda function that defines a condition for filtering the gene's value. Defaults to allowing
 *               all values (i.e., always returns `true`).
 *
 * @return An [Arb] that generates [DoubleGene] instances with the specified configurations.
 */
fun Arb.Companion.doubleGene(
    range: Arb<DoubleRange> = orderedPair(double().filterNot { it.isNaN() || it.isInfinite() })
        .filter { (lo, hi) -> lo < hi }.map { (lo, hi) -> lo..hi },
    value: Arb<Double> = range.map {
        double(it).next()
    },
    filter: (Double) -> Boolean = { true },
) = arbitrary {
    DoubleGene(value.bind(), range.bind(), filter)
}

/**
 * Creates an arbitrary generator for `IntGene` instances, suitable for property-based testing.
 * This generator allows customization of the gene's value, its valid range, and a filter function
 * for additional value constraints. It's an extension function of the `Arb` companion object.
 *
 * @param value An `Arb<Int>` generator for the gene's value. Defaults to a generator for any `Int`.
 * @param range An `Arb<ClosedRange<Int>>` generator for the gene's valid range. Defaults to a
 *              generator that produces any `ClosedRange<Int>`.
 * @param filter A predicate function to apply additional constraints on the gene's value. Defaults
 *               to a function that accepts all values.
 * @return An `Arb<IntGene>` that generates `IntGene` instances with the specified properties.
 */
fun Arb.Companion.intGene(
    value: Arb<Int> = int(),
    range: Arb<ClosedRange<Int>> = range(int(), int()),
    filter: (Int) -> Boolean = { true },
) = arbitrary {
    IntGene(value.bind(), range.bind(), filter)
}
