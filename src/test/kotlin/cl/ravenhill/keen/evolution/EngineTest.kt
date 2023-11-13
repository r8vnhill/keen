package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.checkAll

class EngineTest : FreeSpec({

    "An evolution [Engine]" - {
        "can be created" {
            checkAll(Arb.intGenotypeFactory()) { genotype ->

            }
        }
    }
})
