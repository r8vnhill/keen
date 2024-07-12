package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.listeners.printer.EvolutionPrinter as NewEvolutionPrinter

@Deprecated(
    "This class will be removed in future versions. Use the EvolutionPrinter in the listeners.printer package instead.",
    ReplaceWith("cl.ravenhill.keen.listeners.printer.EvolutionPrinter")
)
class EvolutionPrinter<T, G>(every: Int) : EvolutionListener<T, G> by NewEvolutionPrinter(every)
        where G : Gene<T, G>
