///*
// * "Keen" (c) by R8V.
// * "Keen" is licensed under a
// * Creative Commons Attribution 4.0 International License.
// * You should have received a copy of the license along with this
// * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
// *
// */
//
//
//package cl.ravenhill.keen.util
//
//import cl.ravenhill.keen.Core
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.comparables.shouldBeLessThan
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.*
//import io.kotest.property.checkAll
//import kotlin.random.Random
//
//
//class CollectionsSpec : FreeSpec({
//    "Subset" - {
//        "return a random permutation of the given size" {
//            val n = 10
//            val k = 5
//            val subset = Subset.generateRandomSubset(n, k)
//            subset.size shouldBe k
//            subset.forEach { it shouldBeLessThan n }
//        }
//    }
//    "Subtracting a list by an integer" - {
//        "return a list with each element subtracted by the integer" {
//            checkAll(Arb.list(Arb.double(), 0..10_000), Arb.double()) { list, d ->
//                (list sub d).forEachIndexed { i, e ->
//                    e shouldBe list[i] - d
//                }
//            }
//        }
//    }
//
//    "Swapping two elements in a list should" - {
//        "swap the elements if they are different" {
//            checkAll(Arb.list(Arb.double(), 2..10_000), Arb.long()) { list, seed ->
//                Core.random = Random(seed)
//                val (i, j) = if (list.size == 2) {
//                    0 to 1
//                } else {
//                    Core.random.nextInt(0, list.size - 1) to
//                            Core.random.nextInt(0, list.size - 1)
//                }
//                val copy = list.toMutableList()
//                copy.swap(i, j)
//                copy[i] shouldBe list[j]
//                copy[j] shouldBe list[i]
//            }
//        }
//        "not change the list if the elements are the same" {
//            checkAll(Arb.list(Arb.double(), 2..10_000), Arb.long()) { list, seed ->
//                Core.random = Random(seed)
//                val i = Subset.generateRandomSubset(list.size, 1).first()
//                val copy = list.toMutableList()
//                copy.swap(i, i)
//                copy shouldBe list
//            }
//        }
//        "throw an exception if" - {
//            "the start index is non-positive" {
//                checkAll(
//                    Arb.mutableList(Arb.double()),
//                    Arb.mutableList(Arb.double()),
//                    Arb.nonPositiveInt(),
//
//
//                ) { list1, list2, start ->
//
//                }
//            }
//        }
//    }
//})
//
///**
// * Generates an [Arb]itrary [MutableList] of [E]s.
// *
// * @receiver The [Arb]itrary companion object.
// * @param generator The [Arb]itrary [E] generator.
// * @param E The type of the elements in the list.
// * @return The [Arb]itrary [MutableList] of [E]s.
// */
//fun <E> Arb.Companion.mutableList(generator: Arb<E>) = arbitrary {
//    Arb.list(generator).bind().toMutableList()
//}