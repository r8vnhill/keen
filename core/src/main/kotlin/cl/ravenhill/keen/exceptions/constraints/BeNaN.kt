/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.exceptions.constraints

import cl.ravenhill.jakt.constraints.doubles.DoubleConstraint

/**
 * A constraint object representing the requirement for a `Double` to be NaN (Not a Number).
 *
 * `BeNaN` is a `DoubleConstraint` that encapsulates the condition where a `Double` value must be
 * 'Not a Number' (NaN). This is particularly useful in contexts where NaN values are expected or required,
 * such as in certain mathematical computations or when dealing with exceptional floating-point values.
 *
 * ## Characteristics:
 * - **Validator**: The `validator` property is an implementation of `Double::isNaN`, which checks if a given
 *   `Double` value is NaN.
 *
 * ## Usage:
 * This constraint can be used in validation scenarios where a `Double` is required to be NaN. It is especially
 * relevant in testing or validation frameworks where specific floating-point behaviors need to be enforced or
 * checked.
 *
 * ### Example:
 * ```kotlin
 * val myDouble = Double.NaN
 * val isNan = BeNaN.validator(myDouble) // Returns true as myDouble is NaN
 * ```
 * In this example, `BeNaN` is used to validate that `myDouble` is indeed NaN. The validator property provides a
 * convenient way to perform this check.
 */
data object BeNaN : DoubleConstraint {
    override val validator = Double::isNaN
}
