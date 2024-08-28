package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.utils.Exclusivity

open class CombineCrossover<T, G>(
    val combiner: (List<G>) -> G,
    override val chromosomeRate: Double = TODO(),
    val geneRate: Double = TODO(),
    override val numParents: Int = TODO(),
    override val exclusivity: Exclusivity = TODO()
) : Crossover<T, G> where G : Gene<T, G> {

    val numOffspring: Int = 1

    init {
        constraints {
            "The gene rate must be between 0 and 1" {
                geneRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate must be between 0 and 1" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "Number of parents must be greater or equal to 2" {
                numParents must BeAtLeast(2)
            }
        }
    }
}
