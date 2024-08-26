package cl.ravenhill.keen.evolution.config

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.keen.operators.selection.Selector
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

data class SelectionConfiguration<T, F, R>(
    val survivalRate: Double,
    val parentSelector: Selector<T, F, R>,
    val offspringSelector: Selector<T, F, R>
) where F : Feature<T, F>, R : Representation<T, F> {
    init {
        constraints {
            "Survival rate ($survivalRate) must be between 0 and 1" { survivalRate must BeInRange(0.0..1.0) }
        }
    }
}
