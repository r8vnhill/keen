package cl.ravenhill.keen.utils

/**
 * Enum representing the exclusivity policy in genetic operations such as crossover in evolutionary algorithms.
 *
 * The `Exclusivity` enum defines two distinct exclusivity modes that govern how parent genes are selected and combined
 * during crossover operations in an evolutionary algorithm. These modes determine whether the same parent genes can be
 * repeatedly selected across multiple crossover operations, especially with different partners, or if each gene
 * selection must come from a unique parent within a single crossover operation.
 */
enum class Exclusivity {

    /**
     * Non-exclusive mode allows the same parent gene to be selected multiple times across different crossover
     * operations, even with different partners.
     */
    NON_EXCLUSIVE,

    /**
     * Exclusive mode ensures that each gene comes from a different parent during a single crossover operation,
     * enhancing genetic diversity by preventing over-representation of certain genes across multiple crossovers with
     * different partners.
     */
    EXCLUSIVE
}
