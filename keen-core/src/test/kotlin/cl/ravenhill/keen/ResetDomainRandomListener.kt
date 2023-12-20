/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import io.kotest.core.test.TestCase
import io.kotest.property.PropTestListener
import kotlin.random.Random


/**
 * A property test listener that resets the [Domain.random] to its default value after each test.
 *
 * `ResetDomainRandomListener` is an object implementing the [PropTestListener] interface, specifically designed to
 * reset the `Domain.random` to the [Random.Default] after the execution of each property test. This is particularly
 * useful in scenarios where tests modify the `Domain.random` and a consistent, default state of randomness is needed at
 * the start of each test.
 *
 * ## Usage:
 * Attach `ResetDomainRandomListener` to your property test configurations to ensure that `Domain.random` is reset after
 * each test. This is especially important in a suite of tests where different tests might require different
 * configurations or states of the `Domain.random`.
 *
 * ### Example:
 * ```kotlin
 * class MyPropTests : FreeSpec({
 *     "my test" {
 *         checkAll(
 *             PropTestConfig(listeners = listOf(ResetDomainRandomListener)),
 *             /* other configuration parameters */
 *         ) { /* test body */ }
 *         // Domain.random is reset to Random.Default after each invocation of the test body
 *     }
 * })
 * ```
 * In this example, `ResetDomainRandomListener` is added to the `listeners` parameter of the `PropTestConfig`
 * object, ensuring that `Domain.random` is reset to `Random.Default` after each invocation of the test body.
 */
object ResetDomainRandomListener : PropTestListener {

    /**
     * Overrides the `afterTest` ([PropTestListener.afterTest]]) function to reset the [Domain.random] to its default
     * state.
     *
     * This function is a crucial part of the [PropTestListener] implementation, specifically tailored for scenarios
     * where the global state of `Domain.random` is modified during tests. By overriding `afterTest`, it ensures that
     * `Domain.random` is reset to `Random.Default` after the completion of each iteration of a property  test. This
     * reset is essential for maintaining the integrity and isolation of tests, especially in a testing environment
     * where the randomness behavior needs to be predictable and consistent across different test cases.
     *
     * ## Functionality:
     * - Resets `Domain.random` to `Random.Default` after each test execution.
     * - This approach guarantees that any modifications to the randomness source during a test do not persist
     *   and influence the outcome of subsequent tests.
     */
    override suspend fun afterTest() {
        Domain.random = Random.Default
    }
}
