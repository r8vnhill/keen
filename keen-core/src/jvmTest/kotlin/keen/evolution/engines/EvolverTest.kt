package keen.evolution.engines

import cl.ravenhill.SimpleFeature
import cl.ravenhill.SimpleRepresentation
import cl.ravenhill.keen.evolution.states.SimpleEvolutionState
import io.kotest.core.spec.style.FreeSpec

private typealias MyRepresentation = SimpleRepresentation<Int, SimpleFeature>

private typealias MyEvolutionState = SimpleEvolutionState<Int, SimpleFeature, MyRepresentation>

class EvolverTest : FreeSpec({
    "An Evolver" - {}
})

//class SimpleEvolver : AbstractEvolver<Int, SimpleFeature, MyRepresentation, MyEvolutionState>()
