/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.trees

interface Node<T>

interface Intermediate<T> : Node<T> {
    val arity: Int
}

interface Leaf<T> : Node<T>