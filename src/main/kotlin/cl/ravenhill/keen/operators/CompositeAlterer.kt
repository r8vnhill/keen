package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Population


class CompositeAlterer<DNA>(private val alterers: List<Alterer<DNA>>) : AbstractAlterer<DNA>(1.0) {
    override fun invoke(population: Population<DNA>) =
        alterers.fold(population.toList()) { acc, alterer ->
            alterer(acc)
        }
}