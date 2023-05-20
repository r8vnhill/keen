/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.enforcer.requirements


/**
 * A [PairRequirement] is a [Requirement] that specifies constraints on a [Pair] of values of types
 * [T] and [U].
 *
 * @param T The type of the first element of the pair.
 * @param U The type of the second element of the pair.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
sealed interface PairRequirement<T, U> : Requirement<Pair<T, U>> {

    // Inherit documentation from Requirement
    override fun generateException(description: String) =
        cl.ravenhill.enforcer.PairRequirementException { description }

    /**
     * [BeStrictlyOrdered] is a [PairRequirement] that requires that the first element in a [Pair]
     * is strictly less than the second element, where both elements must be of the same comparable
     * type [A].
     */
    class BeStrictlyOrdered<A : Comparable<A>> : PairRequirement<A, A> {
        override val validator = { value: Pair<A, A> -> value.first < value.second }
    }

    /**
     * [BeFinite] is a [PairRequirement] that requires that both elements in a [Pair] are finite
     * doubles.
     */
    data object BeFinite : PairRequirement<Double, Double> {
        override val validator =
            { value: Pair<Double, Double> -> value.first.isFinite() && value.second.isFinite() }
    }
}