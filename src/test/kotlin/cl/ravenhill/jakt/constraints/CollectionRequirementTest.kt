/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.jakt.constraints

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.jakt.constraints.collections.CollectionConstraint
import cl.ravenhill.jakt.constraints.collections.HaveElement
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.arbs.datatypes.any
import cl.ravenhill.keen.arbs.datatypes.mutableList
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll

/***************************************************************************************************
 * This file defines CollectionRequirementTest, a suite of tests for validating collection
 * requirements.
 * It uses the FreeSpec style from the Kotlin testing library, Kotest.
 * The tests verify that an unfulfilled collection requirement generates a
 * CollectionRequirementException, and that the validate function correctly handles empty
 * collections according to the specified requirements.
 * The file also includes helper methods to generate arbitrary collection requirements for use in
 * these tests.
 **************************************************************************************************/

/**
 * A suite of tests for validating collection requirements.
 *
 * @see Arb.Companion.requirement
 * @see CollectionConstraintException
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
@OptIn(ExperimentalKotest::class)
class CollectionRequirementTest : FreeSpec({
    "Generating an exception should return a CollectionRequirementException" {
        checkAll(Arb.requirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<cl.ravenhill.jakt.exceptions.CollectionConstraintException>()
                message shouldBe "Unfulfilled constraint: $description"
            }
        }
    }

    "Validating that a collection is empty should return" - {
        "[true] if the collection is empty" {
            BeEmpty.validator(emptyList<Any>()).shouldBeTrue()
        }

        "[false] if the collection is not empty" {
            checkAll(Arb.list(Arb.any(), 1..100)) { list ->
                BeEmpty.validator(list).shouldBeFalse()
            }
        }
    }

    "Validating that a collection have a given size should" - {
        "return [true] if the collection has the given size" {
            checkAll(Arb.list(Arb.any(), 1..100)) { list ->
                HaveSize(list.size).validator(list).shouldBeTrue()
            }
        }

        "return [false] if the collection does not have the given size" {
            checkAll(Arb.list(Arb.any(), 1..100), Arb.int(1..100)) { list, size ->
                assume { list.size shouldNotBe size }
                HaveSize(size).validator(list).shouldBeFalse()
            }
        }
    }

    "Validating that a collection includes a given element should" - {
        "return [true] if the collection includes the given element" {
            checkAll(Arb.mutableList(Arb.any(), 1..100), Arb.any()) { list, element ->
                list += element
                HaveElement(element).validator(list).shouldBeTrue()
            }
        }

        "return [false] if the collection does not include the given element" {
            checkAll(
                PropTestConfig(iterations = 89),
                Arb.list(Arb.any(), 1..50),
                Arb.any()
            ) { list, element ->
                assume { list shouldNotContain element }
                HaveElement(element).validator(list).shouldBeFalse()
            }
        }
    }
})

/**
 * Creates an arbitrary for generating a requirement.
 *
 * @see CollectionConstraint.BeEmpty
 */
private fun Arb.Companion.requirement(): Arb<CollectionConstraint> =
    arbitrary { element(BeEmpty, HaveSize(int().bind())).bind() }
