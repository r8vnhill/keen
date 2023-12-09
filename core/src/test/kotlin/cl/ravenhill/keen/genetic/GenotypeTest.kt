/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.assertions.`test Genotype Factory behaviour`
import cl.ravenhill.keen.assertions.`test Genotype behaviour`
import cl.ravenhill.keen.assertions.`test Genotype creation`
import cl.ravenhill.keen.assertions.`test Genotype verification`
import io.kotest.core.spec.style.FreeSpec

class GenotypeTest : FreeSpec({
    include(`test Genotype creation`())
    include(`test Genotype verification`())
    include(`test Genotype behaviour`())
    include(`test Genotype Factory behaviour`())
})
