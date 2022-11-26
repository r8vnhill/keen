package cl.ravenhill.keen.constraints

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype

class RetryConstraint<DNA>(
    genotype: Genotype.Factory<DNA>,
    validator: (Phenotype<DNA>) -> Boolean = Phenotype<DNA>::verify,
    maxRetries: Int = 10
) : Constraint<DNA>
