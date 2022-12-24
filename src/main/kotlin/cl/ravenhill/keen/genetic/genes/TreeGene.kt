package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.util.trees.ListTree


interface TreeGene<DNA : Any> : Gene<DNA>, ListTree<DNA> {
    val childCount: Int
}

abstract class AbstractTreeGene<DNA : Any>(
    override val dna: DNA,
    override val childCount: Int
) : TreeGene<DNA>