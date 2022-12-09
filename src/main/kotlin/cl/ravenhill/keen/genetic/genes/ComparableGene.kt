package cl.ravenhill.keen.genetic.genes

/**
 * A [Gene] that can be compared to other [ComparableGene]s.
 *
 * @param DNA The type of the value of the gene (e.g. [Int], [Double], etc).
 */
interface ComparableGene<DNA : Comparable<DNA>> : Gene<DNA> {

    /**
     * Compares this gene to the given one.
     *
     * @return A negative integer, zero, or a positive integer as this gene is less than, equal to,
     * or greater than the given one.
     */
    operator fun compareTo(other: ComparableGene<DNA>) = dna.compareTo(other.dna)
}