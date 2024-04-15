/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.assertions.*
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class GenotypeTest : FreeSpec({
    include(`test Genotype creation`())
    include(`test Genotype verification`())
    include(`test Genotype behavior`())
    include(`test Genotype Factory behaviour`())
    include(`test Genotype emptiness`())
    include(`test Genotype contains`())
})
