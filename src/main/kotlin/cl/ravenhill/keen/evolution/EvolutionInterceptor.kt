package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.validateAtLeast


class EvolutionInterceptor<DNA>(
    val before: (EvolutionStart<DNA>) -> EvolutionStart<DNA>,
    val after: (EvolutionResult<DNA>) -> EvolutionResult<DNA>
) {
    companion object {
        fun <DNA> identity() = EvolutionInterceptor(
            { before: EvolutionStart<DNA> -> before }
        ) { end: EvolutionResult<DNA> -> end }
    }
}

class EvolutionStart<T>(val population: List<Phenotype<T>>, val generation: Int) {

    init {
        generation.validateAtLeast(0) { "Generation must be non-negative" }
    }

    override fun toString() = "EvolutionStart { " +
            "population: $population, " +
            "generation: $generation, " +
            " }"

    companion object {
        fun <T> empty(): EvolutionStart<T> = EvolutionStart(listOf(), 1)
    }
}

//class EvolutionResult<DNA> {
//    fun bestPhenotype(): Phenotype<DNA> {
//        TODO("Not yet implemented")
//    }
//
//    companion object {
////        fun <DNA> toBestPhenotype(): Collector<EvolutionResult<DNA>, *, Phenotype<DNA>> {
////            return Collector.of(
////                MinMax<EvolutionResult<DNA>>::of,
////                MinMax<EvolutionResult<DNA>>::accept,
////                MinMax<EvolutionResult<DNA>>::combine,
////                Function<MinMax<EvolutionResult<DNA>>, Phenotype<DNA>?> { mm: MinMax<EvolutionResult<DNA>> ->
////                    mm.max().bestPhenotype()
////                }
////            )
////        }
//
//        private fun bestPhenotype(): Nothing? {
//            TODO("Not yet implemented")
//        }
//
//    }

//}

