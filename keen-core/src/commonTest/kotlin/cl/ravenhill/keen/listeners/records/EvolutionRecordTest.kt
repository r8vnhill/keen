package cl.ravenhill.keen.listeners.records

import cl.ravenhill.SimpleFeature
import cl.ravenhill.SimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty

class EvolutionRecordTest : FreeSpec({
    "An evolution record" - {
        "should start wih an empty list of generations by default" {
            val record = EvolutionRecord<_, _, SimpleRepresentation<Int, SimpleFeature>>()
            record.generations.shouldBeEmpty()
        }

        "should throw a exception"
    }
})

