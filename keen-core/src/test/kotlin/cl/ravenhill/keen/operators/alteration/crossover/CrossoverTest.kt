package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.assertions.`crossover should return a list of offspring`
import cl.ravenhill.keen.assertions.`crossover should throw an exception on incorrect configuration`
import io.kotest.core.spec.style.FreeSpec

class CrossoverTest : FreeSpec({
    "Crossing a list of parents" - {
        `crossover should return a list of offspring`()
        `crossover should throw an exception on incorrect configuration`()
    }
})

