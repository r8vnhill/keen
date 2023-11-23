/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.assertions.evolution.`test Engine evaluation`
import cl.ravenhill.keen.assertions.evolution.`test Engine evolution start`
import cl.ravenhill.keen.assertions.evolution.`test Engine offspring selection`
import io.kotest.core.spec.style.FreeSpec


class EvolutionEngineTest : FreeSpec({

    include(`test Engine evolution start`())
    include(`test Engine evaluation`())
    include(`test Engine offspring selection`())
})
