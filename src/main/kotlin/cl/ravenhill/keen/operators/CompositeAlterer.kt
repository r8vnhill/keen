package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Population


class CompositeAlterer<DNA>(private val alterers: List<Alterer<DNA>>) : AbstractAlterer<DNA>(1.0) {
    override fun invoke(population: Population<DNA>, generation: Int): AltererResult<DNA> {
        var result = AltererResult(population)
        for (alterer in alterers) {
            val altererResult = alterer(result.population, generation)
            result = AltererResult(altererResult.population, result.alterations + altererResult.alterations)
        }
        return result
    }
}