/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int

/**
 * Generates an arbitrary [BoolGene] value, either [BoolGene.True] or [BoolGene.False].
 */
fun Arb.Companion.boolGene() = arbitrary { element(BoolGene.True, BoolGene.False).bind() }

/**
 * Generates an [Arb] (Arbitrary) of [CharGene] from the given character arbitrary.
 *
 * @param c The arbitrary source of characters to construct the CharGene.
 * @return An arbitrary of CharGene.
 */
fun Arb.Companion.charGene(c: Arb<Char> = char()) = arbitrary {
    CharGene(c.bind())
}

/**
 * Creates an arbitrary gene instance containing a double value from the provided arbitrary double source.
 *
 * @param d An arbitrary source of double values.
 * @return An arbitrary gene instance with a double value.
 */
fun Arb.Companion.doubleGene(d: Arb<Double> = double()) = arbitrary {
    DoubleGene(d.bind())
}

/**
 * Creates an arbitrary generator for an `IntGene` using the provided arbitrary generator for
 * integers.
 *
 * @param i The arbitrary generator for integers which will be bound to the `IntGene`.
 * @return An arbitrary generator that produces instances of `IntGene`.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
fun Arb.Companion.intGene(i: Arb<Int> = int()) = arbitrary {
    IntGene(i.bind())
}
