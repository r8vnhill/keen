package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.utils.Exclusivity
import kotlin.random.Random

class UniformCrossover<T, G>(
    numParents: Int = TODO(),
    chromosomeRate: Double = TODO(),
    geneRate: Double = TODO(),
    exclusivity: Exclusivity = TODO(),
    random: Random = Domain.random
) : CombineCrossover<T, G>(
    combiner = { genes -> genes.random(random) },
    chromosomeRate = chromosomeRate,
    geneRate = geneRate,
    numParents = numParents,
    exclusivity = exclusivity
) where G : Gene<T, G>
