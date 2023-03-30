package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal

/***************************************************************************************************
 * This code defines functions to generate random programs in the form of breadth-first trees given
 * a list of functions and terminals.
 **************************************************************************************************/

/**
 * Generates a random program using a random method from a list of generation methods.
 *
 * @param methods the list of generation methods.
 * @param terminals the list of terminals.
 * @param functions the list of functions.
 * @param min the minimum depth of the program.
 * @param max the maximum depth of the program.
 *
 * @return a random program as a breadth-first tree.
 */
fun <T> generateProgramWith(
    methods: List<(List<Terminal<T>>, List<Fun<T>>, Int, Int) -> Program<T>>,
    terminals: List<Terminal<T>>,
    functions: List<Fun<T>>,
    min: Int,
    max: Int
) = methods.random(Core.random).invoke(terminals, functions, min, max)

/**
 * Generates a program where each leaf might have different depth.
 *
 * @param terminals the list of terminals.
 * @param functions the list of functions.
 * @param min the minimum depth of the program.
 * @param max the maximum depth of the program.
 *
 * @return a random program as a breadth-first tree.
 */
fun <T> generateProgramGrowing(
    terminals: List<Terminal<T>>, functions: List<Fun<T>>, min: Int, max: Int
): Program<T> {
    val condition = { height: Int, depth: Int ->
        depth == height || (
                depth >= min &&
                        Core.Dice.probability()
                        < terminals.size.toDouble() / (terminals.size + functions.size))
    }
    return generateProgram(functions, terminals, min, max, condition)
}

/**
 * Generates a program where each leaf has the same depth.
 *
 * @param terminals the list of terminals.
 * @param functions the list of functions.
 * @param min the minimum depth of the program.
 * @param max the maximum depth of the program.
 *
 * @return a random program as a breadth-first tree.
 */
fun <T> generateProgramFull(
    terminals: List<Terminal<T>>, functions: List<Fun<T>>, min: Int, max: Int
): Program<T> {
    val condition = { height: Int, depth: Int ->
        depth == height
    }
    return generateProgram(functions, terminals, min, max, condition)
}

/**
 * Generates a random program.
 * The tree is built from the root to the leaves, and it stops growing the current branch when
 * the condition is met; in which case, it backtracks to the root and starts a new branch.
 *
 * @param functions the list of functions.
 * @param terminals the list of terminals.
 * @param min the minimum height of the program tree.
 * @param max the maximum height of the program tree.
 * @param condition the condition to stop growing the current branch.
 *
 * @return a random program as a breadth-first tree.
 */
fun <T> generateProgram(
    functions: List<Fun<T>>,
    terminals: List<Terminal<T>>,
    min: Int,
    max: Int,
    condition: (Int, Int) -> Boolean
): Program<T> {
    val program = mutableListOf<ProgramNode<T>>()
    val height = Core.random.nextInt(min, max)
    val depths = mutableListOf(0)
    while (depths.isNotEmpty()) {
        val depth = depths.removeLast()
        val node = if (condition(height, depth)) {
            ProgramNode(terminals.random(Core.random), depth)
        } else {
            ProgramNode(functions.random(Core.random), depth).also { node ->
                depths.addAll(List(node.arity) { depth + 1 })
            }
        }
        program.add(node)
    }
    return Program(program)
}
