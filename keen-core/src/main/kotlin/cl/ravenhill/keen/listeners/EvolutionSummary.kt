package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.summary.EvolutionSummary as NewEvolutionSummary

@Deprecated(
    "This class will be removed in future versions. Use cl.ravenhill.keen.listeners.summary.EvolutionSummary instead.",
    ReplaceWith("cl.ravenhill.keen.listeners.summary.EvolutionSummary")
)
class EvolutionSummary<T, G> : EvolutionListener<T, G> by NewEvolutionSummary()
        where G : Gene<T, G>
