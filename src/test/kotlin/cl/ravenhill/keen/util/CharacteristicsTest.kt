/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue


class CharacteristicsTest : FreeSpec({
    "A [Verifiable] should always verify to true by default" {
        val verifiable = object : Verifiable {}
        verifiable.verify().shouldBeTrue()
    }
})