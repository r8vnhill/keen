/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class ComparableGeneTest : FreeSpec({

    "A Comparable Gene can be compared with another" {
        checkAll<Int, Int> { a, b ->
            ComparableGeneImpl(a).compareTo(ComparableGeneImpl(b)) shouldBe a.compareTo(b)
        }
    }
}) {

    private class ComparableGeneImpl(override val value: Int) : ComparableGene<Int, ComparableGeneImpl> {
        override fun duplicateWithValue(value: Int) = ComparableGeneImpl(value)
    }
}