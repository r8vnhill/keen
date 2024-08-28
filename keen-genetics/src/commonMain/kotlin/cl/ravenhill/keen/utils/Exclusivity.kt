package cl.ravenhill.keen.utils

/**
 * Enum representing the exclusivity policy in genetic operations such as crossover in evolutionary algorithms.
 *
 * The `Exclusivity` enum defines two possible exclusivity modes for genetic operations like crossover in an
 * evolutionary algorithm. These modes determine how parent genes are selected and combined during the crossover
 * process, specifically whether the same parent genes can be selected more than once (non-exclusive) or whether each
 * gene must come from a distinct parent (exclusive).
 *
 * @see NON_EXCLUSIVE
 * @see EXCLUSIVE
 */
enum class Exclusivity {

    /**
     * Non-exclusive mode allows the same parent gene to be selected multiple times during the crossover process.
     */
    NON_EXCLUSIVE,

    /**
     * Exclusive mode ensures that each gene comes from a different parent, promoting genetic diversity.
     */
    EXCLUSIVE
}
