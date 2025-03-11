package cl.ravenhill.keen.constraints

import cl.ravenhill.jakt.constraints.doubles.DoubleConstraint

object BeProbability : DoubleConstraint {
    override val validator: (Double) -> Boolean
        get() = {
            it in 0.0..1.0
        }
}
