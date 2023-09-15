/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class ComparableGeneTest : FreeSpec({
    "A [ComparableGene] can be compared with another" {
        checkAll<Int, Int> { a, b ->
            ComparableGeneImpl(a).compareTo(ComparableGeneImpl(b)) shouldBe a.compareTo(b)
        }
    }
})

private class ComparableGeneImpl(override val dna: Int) : ComparableGene<Int, ComparableGeneImpl> {
    override fun withDna(dna: Int) = ComparableGeneImpl(dna)
}
