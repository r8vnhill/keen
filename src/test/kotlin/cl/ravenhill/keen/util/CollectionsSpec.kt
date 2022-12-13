/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 *
 */


package cl.ravenhill.keen.util

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll


class CollectionsSpec : WordSpec({
    "Subset" should {
        "return a random permutation of the given size" {
            val n = 10
            val k = 5
            val subset = Subset.next(n, k)
            subset.size shouldBe k
            subset.forEach { it shouldBeLessThan n }
        }
    }
    "Subtracting a list by an integer" should {
        "return a list with each element subtracted by the integer" {
            checkAll(Arb.list(Arb.double(), 0..10_000), Arb.double()) { list, d ->
                (list sub d).forEachIndexed { i, e ->
                    e shouldBe list[i] - d
                }
            }
        }
    }
})