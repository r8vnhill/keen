/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.exceptions.AbsurdOperation

/**
 * A theoretical representation of a gene that carries no value, implemented for testing purposes and completeness of
 * the gene type system.
 *
 * `NothingGene` is a data object that represents a gene with a type of `Nothing`. This gene is a conceptual entity and
 * is not meant for practical use in applications. It is primarily used for testing purposes and to demonstrate the
 * completeness and robustness of the gene type system in a theoretical context.
 *
 * ## Characteristics:
 * - **Value**: The `value` property is of type `Nothing` and accessing it will always throw an `AbsurdOperation`
 *   exception. This is because `Nothing` is a type that cannot have any instances.
 * - **Duplication**: Any attempt to duplicate this gene using `duplicateWithValue` will also throw an
 *   `AbsurdOperation` exception, as there can be no valid value of type `Nothing`.
 *
 * ## Usage:
 * This object is not intended for use in actual evolutionary algorithms or genetic programming. It exists solely to
 * fulfill the theoretical aspects of the gene type system and to serve as a placeholder or edge case in testing
 * environments.
 *
 * ### Example:
 * ```kotlin
 * // Theoretical usage, not meant for practical implementation
 * try {
 *     val gene = NothingGene.value // This will throw an AbsurdOperation exception
 * } catch (t: AbsurdOperation) {
 *     println("Attempted to access value of NothingGene: $t")
 * }
 *
 * try {
 *     val duplicatedGene = NothingGene.duplicateWithValue( /* No valid value can be provided */ )
 * } catch (t: AbsurdOperation) {
 *     println("Attempted to duplicate NothingGene: $t")
 * }
 * ```
 * In these examples, any interaction with `NothingGene` results in an `AbsurdOperation` exception, highlighting its
 * theoretical and non-functional nature.
 *
 * @property value An inaccessible property that always throws `AbsurdOperation` when accessed.
 */
data object NothingGene : Gene<Nothing, NothingGene> {

    override val value: Nothing
        get() = throw AbsurdOperation

    /**
     * Absurd method; inaccessible due to the inherent lack of [value] in a `NothingGene`.
     *
     * This method is a theoretical implementation that aligns with the `Gene` interface requirements. However, since
     * `NothingGene` represents a gene of type `Nothing` and `Nothing` cannot have any instances, this method is not
     * practically usable. The `AbsurdOperation` throwable signifies the absurdity of attempting to assign a value to
     * a `Nothing` type, and thus, in practice, this method is unreachable and the exception will never be thrown.
     *
     * ## Theoretical Context:
     * This method exists to maintain the structural integrity of the gene type system in a theoretical context. It
     * demonstrates how a gene with a `Nothing` type would behave if it were possible to instantiate or duplicate such
     * a gene.
     *
     * ## Usage:
     * This method is not intended for use in actual evolutionary algorithms. It serves a purely theoretical purpose
     * and is included for completeness and to satisfy interface requirements.
     *
     * ### Example:
     * ```kotlin
     * // Theoretical usage, not meant for practical implementation
     * try {
     *     val duplicatedGene = NothingGene.duplicateWithValue( /* No valid value can be provided */ )
     * } catch (e: AbsurdOperation) {
     *     // This catch block is theoretically unreachable
     *     println("Attempted to duplicate NothingGene: ${e.message}")
     * }
     * ```
     * In this example, the catch block for the `AbsurdOperation` is theoretically unreachable because `NothingGene`
     * cannot be instantiated or duplicated in a practical scenario.
     */
    override fun duplicateWithValue(value: Nothing) = throw AbsurdOperation
}
