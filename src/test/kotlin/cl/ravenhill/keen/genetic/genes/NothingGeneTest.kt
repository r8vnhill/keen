/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.IllegalOperationException
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FreeSpec


/**
 * `NothingGeneTest` is a unit test class designed to verify the behavior of the `NothingGene`
 * in specific scenarios, particularly around illegal operations.
 *
 * The class is structured to test various functions of `NothingGene`, including accessing its DNA,
 * mutation, and flattening. For each of these operations, it is expected that an
 * `IllegalOperationException` will be thrown due to the nature of the `NothingGene`.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class NothingGeneTest : FreeSpec({
    "A __Nothing Gene__" - {
        "should throw an `IllegalOperationException` when" - {
            "accessing its DNA" {
                `check that an illegal operation exception is thrown` { NothingGene.dna }
            }

            "mutating" {
                `check that an illegal operation exception is thrown` { NothingGene.mutate() }
            }

            "flattening" {
                `check that an illegal operation exception is thrown` { NothingGene.flatten() }
            }
        }
    }
})

/**
 * A utility function designed to verify that a block of code results in an
 * [IllegalOperationException].
 * This function encapsulates the repetitive pattern of checking for the specific exception and its
 * expected message, which simplifies the main test cases.
 *
 * @param block The block of code to execute and check for the expected exception.
 */
private fun `check that an illegal operation exception is thrown`(block: () -> Unit) {
    shouldThrowWithMessage<IllegalOperationException>("Illegal operation: A NothingGene has no DNA") {
        block()
    }
}
