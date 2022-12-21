/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 *
 */


package cl.ravenhill.keen.util

import cl.ravenhill.keen.Core
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random


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

    "Swap" When {
        "Swapping two elements in a list" should {
            "swap the elements if they are different" {
                checkAll(Arb.list(Arb.double(), 2..10_000), Arb.long()) { list, seed ->
                    Core.random = Random(seed)
                    val (i, j) = if (list.size == 2) {
                        0 to 1
                    } else {
                        Core.random.nextInt(0, list.size - 1) to Core.random.nextInt(0, list.size - 1)
                    }
                    val copy = list.toMutableList()
                    copy.swap(i, j)
                    copy[i] shouldBe list[j]
                    copy[j] shouldBe list[i]
                }
            }
            "not change the list if the elements are the same" {
                checkAll(Arb.list(Arb.double(), 2..10_000), Arb.long()) { list, seed ->
                    Core.random = Random(seed)
                    val i = Subset.next(list.size, 1).first()
                    val copy = list.toMutableList()
                    copy.swap(i, i)
                    copy shouldBe list
                }
            }
        }
    }
})