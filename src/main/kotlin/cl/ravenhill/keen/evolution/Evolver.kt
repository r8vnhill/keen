package cl.ravenhill.keen.evolution


interface Evolver {
    fun <DNA> evolve(next: EvolutionStart<DNA>): EvolutionResult<DNA>
}