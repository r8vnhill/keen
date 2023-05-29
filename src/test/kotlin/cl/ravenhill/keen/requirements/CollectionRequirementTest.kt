/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.requirements

import cl.ravenhill.any
import cl.ravenhill.enforcer.CollectionRequirementException
import cl.ravenhill.enforcer.requirements.CollectionRequirement
import cl.ravenhill.enforcer.requirements.CollectionRequirement.BeEmpty
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
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
 * @see CollectionRequirement
 * @see CollectionRequirement.BeEmpty
 * @see CollectionRequirementException
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class CollectionRequirementTest : FreeSpec({
    "Generating an exception should return a CollectionRequirementException" {
        checkAll(Arb.requirement(), Arb.string()) { requirement, description ->
            with(requirement.generateException(description)) {
                shouldBeInstanceOf<CollectionRequirementException>()
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
})

/**
 * Creates an arbitrary for generating a requirement.
 */
private fun Arb.Companion.requirement() = arbitrary { element(BeEmpty).bind() }
