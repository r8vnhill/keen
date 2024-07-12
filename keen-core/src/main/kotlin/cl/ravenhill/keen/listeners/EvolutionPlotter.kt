package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvaluationListener
import cl.ravenhill.keen.listeners.plotter.EvolutionPlotter as NewEvolutionPlotter


@Deprecated(
    "This class will be removed in future versions. Use the EvolutionPlotter in the listeners.plotter package instead.",
    ReplaceWith("cl.ravenhill.keen.listeners.plotter.EvolutionPlotter")
)
class EvolutionPlotter<T, G> : EvaluationListener<T, G> by NewEvolutionPlotter()
        where G : Gene<T, G>
