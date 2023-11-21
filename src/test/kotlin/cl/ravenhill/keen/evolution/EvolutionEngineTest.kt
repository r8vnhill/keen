/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.assertions.engine.`check Engine alteration`
import cl.ravenhill.keen.assertions.engine.`check Engine evaluation`
import cl.ravenhill.keen.assertions.engine.`check Engine evolution start`
import cl.ravenhill.keen.assertions.engine.`check Engine offspring selection`
import cl.ravenhill.keen.assertions.engine.`check Engine single step evolution`
import cl.ravenhill.keen.assertions.engine.`check Engine survivor selection`
import io.kotest.core.spec.style.FreeSpec

class EvolutionEngineTest : FreeSpec({

    "An evolution Engine" - {

        `check Engine evolution start`()

        `check Engine evaluation`()

        `check Engine offspring selection`()

        `check Engine survivor selection`()

        `check Engine alteration`()

        `check Engine single step evolution`()
    }
})
