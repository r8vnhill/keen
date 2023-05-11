/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.problems.ga.stacktrace

import kotlin.reflect.KFunction

class Tracer<T>(val statements: List<KFunction<*>>) {
    private val inputFactory = InputFactory()

    companion object {
        inline fun <reified T> create(): Tracer<T> =
            Tracer(T::class.members.filterIsInstance<KFunction<*>>())
    }
}

fun main() {
    val tracer = Tracer.create<Tracer<*>>()
    println(tracer.statements.joinToString("\n"))
}