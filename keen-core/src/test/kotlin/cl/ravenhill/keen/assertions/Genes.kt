/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.exceptions.AbsurdOperation
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.mixins.Filterable
import cl.ravenhill.keen.mixins.Ranged
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

fun `check that an Absurd Operation is thrown`(block: () -> Unit) = shouldThrow<AbsurdOperation>(block)

suspend fun <T, G> FreeSpecContainerScope.`test that the gene value is set to the expected value`(
    arb: Arb<T>,
    geneFactory: (T) -> G,
) where G : Gene<T, G> {
    "should have a value property that is set to the value provided in the constructor" {
        checkAll(arb) { value ->
            geneFactory(value).value shouldBe value
        }
    }
}

suspend fun <T, G> FreeSpecContainerScope.`test that the gene range is set to the expected range`(
    arb: Arb<T>,
    defaultRange: ClosedRange<T>,
    defaultRangeFactory: (T) -> G,
    arbRangeFactory: (T, ClosedRange<T>) -> G,
) where G : Gene<T, G>, G : Ranged<T>, T : Comparable<T> {
    "should have a range property that" - {
        "defaults to the range Int.MIN_VALUE..Int.MAX_VALUE" {
            checkAll(arb) { value ->
                defaultRangeFactory(value).range shouldBe defaultRange
            }
        }

        "is set to the range provided in the constructor" {
            checkAll(arb, arbRange(arb, arb)) { value, range ->
                arbRangeFactory(value, range).range shouldBe range
            }
        }
    }
}

suspend fun <T, G> FreeSpecContainerScope.`test that the gene filter is set to the expected filter`(
    arb: Arb<T>,
    defaultFilterFactory: (T) -> G,
    arbFilterFactory: (T, (T) -> Boolean) -> G,
    filter: (T) -> Boolean,
) where G : Gene<T, G>, G : Filterable<T> {
    "should have a filter property that" - {
        "defaults to a filter that always returns true" {
            checkAll(arb) { value ->
                defaultFilterFactory(value).filter(value) shouldBe true
            }
        }

        "is set to the filter provided in the constructor" {
            checkAll(arb) { value ->
                arbFilterFactory(value, filter).filter(value) shouldBe filter(value)
            }
        }
    }
}

suspend fun <T, G> FreeSpecContainerScope.`test that a gene can generate a value`(
    arb: Arb<T>,
    geneFactory: (T) -> G,
    generator: (Random, ClosedRange<T>) -> T,
) where T : Comparable<T>, G : Gene<T, G>, G : Ranged<T> {
    "can generate a random value" {
        checkAll(arb, Arb.long().map { Random(it) to Random(it) }) { value, (r1, r2) ->
            Domain.random = r1
            val gene = geneFactory(value)
            gene.generator() shouldBe generator(r2, gene.range)
        }
    }
}

suspend fun <T, G> FreeSpecContainerScope.`test that a gene can duplicate itself`(
    arb: Arb<T>,
    gene: Arb<G>
) where G : Gene<T, G>, G: Ranged<T>, G: Filterable<T> {
    "can create a copy with a different value" {
        checkAll(gene, arb) { gene, newValue ->
            val copy = gene.duplicateWithValue(newValue)
            copy.value shouldBe newValue
            copy.range shouldBe gene.range
            copy.filter shouldBe gene.filter
        }
    }
}
