/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


// "Unused" receivers are used to allow the DSL to define functions that are only available inside a scope.
@file:Suppress("UnusedReceiverParameter")

package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.genetics.chromosomes.BooleanChromosomeFactory
import cl.ravenhill.keen.genetics.chromosomes.ChromosomeFactory
import cl.ravenhill.keen.genetics.genes.Gene

class ChromosomeScope<T>

fun <T, G> GenotypeScope<T, G>.chromosomeOf(
    lazyFactory: ChromosomeScope<T>.() -> ChromosomeFactory<T, G>
) where G : Gene<T, G> = chromosomes.add(ChromosomeScope<T>().lazyFactory())

fun ChromosomeScope<Boolean>.booleans(builder: BooleanChromosomeFactory.() -> Unit) =
    BooleanChromosomeFactory().apply(builder)
