package cl.ravenhill.keen.evolution


interface Evolver<DNA> {
    fun evolve(next: EvolutionStart<DNA>): EvolutionResult<DNA>
}