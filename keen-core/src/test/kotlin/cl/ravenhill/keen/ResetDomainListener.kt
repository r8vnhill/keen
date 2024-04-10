/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.keen.prog.Program
import io.kotest.property.PropTestListener
import kotlin.random.Random


/**
 * A singleton object that implements `PropTestListener` to reset the domain parameters to their defaults after each property test.
 *
 * `ResetDomainListener` is designed to ensure that the domain settings used in property-based testing are reset to
 * their default values after each test is executed. This is crucial in a testing environment where domain parameters
 * like randomness, program depth, and equality threshold might be altered during tests, and consistent starting
 * conditions are needed for each test case.
 *
 * ## Key Functions:
 * - `afterTest`: The primary function invoked after each test execution. It resets the domain parameters to their
 *   default values.
 *
 * ## Usage:
 * This listener is typically used in property-based testing frameworks where tests might modify global domain settings.
 * By attaching `ResetDomainListener` to the test framework, it ensures that each test starts with a clean and
 * predictable environment.
 *
 * ### Example:
 * In a property-based testing framework, you can attach `ResetDomainListener` to ensure domain settings are reset:
 * ```kotlin
 * checkAll(
 *    PropTestConfig(listeners = listOf(ResetDomainListener)),
 *    /* Arbitrary generators and property tests */
 * ) { /* Test body */ }
 * ```
 * In this example, `ResetDomainListener` is used to reset domain settings after a specific property test.
 *
 * @OptIn Annotation: Marks the `afterTest` function as using experimental features from the Keen library,
 * specifically the [Program.DEFAULT_MAX_DEPTH] constant.
 */
object ResetDomainListener : PropTestListener {
    @OptIn(ExperimentalKeen::class)
    override suspend fun afterTest() {
        Domain.random = Random.Default
        Domain.maxProgramDepth = Program.DEFAULT_MAX_DEPTH
        Domain.equalityThreshold = Domain.DEFAULT_EQUALITY_THRESHOLD
    }
}
