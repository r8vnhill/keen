package cl.ravenhill.keen.evolution


interface Evolver<DNA> {
    fun evolve(start: EvolutionStart<DNA>): EvolutionResult<DNA>
}