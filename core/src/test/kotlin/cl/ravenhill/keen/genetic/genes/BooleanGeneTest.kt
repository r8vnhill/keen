package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.genetic.genes.booleanGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

class BooleanGeneTest : FreeSpec({

    "A Boolean Gene" - {
        "should be able to generate random values" {
            checkAll(Arb.booleanGene(), Arb.long().map { Random(it) to Random(it) }) { gene, (r1, r2) ->
                Domain.random = r1
                val randomValue = gene.generator()
                randomValue shouldBe r2.nextBoolean()
            }
        }
    }
})