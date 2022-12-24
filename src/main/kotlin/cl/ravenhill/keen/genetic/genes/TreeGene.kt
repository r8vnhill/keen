package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.util.trees.ArrayTree


interface TreeGene<DNA : Any> : Gene<DNA>, ArrayTree<DNA> {
    val childOffset: Int
    val childCount: Int
}

abstract class AbstractTreeGene<DNA : Any>(
    override val dna: DNA,
    override val childOffset: Int,
    override val childCount: Int
) : TreeGene<DNA>