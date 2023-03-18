package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Terminal

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
    methods: List<(List<Terminal<T>>, List<Fun<T>>, Int, Int) -> List<ProgramNode<T, Reduceable<T>>>>,
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
): List<ProgramNode<T, Reduceable<T>>> {
    val condition = { height: Int, depth: Int ->
        depth == height || (depth >= min && Core.Dice.probability() < terminals.size / (terminals.size + functions.size))
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
): List<ProgramNode<T, Reduceable<T>>> {
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
 * @param functions List<Fun<T>>
 * @param terminals List<Terminal<T>>
 * @param min Int
 * @param max Int
 * @param condition Function2<Int, Int, Boolean>
 * @return List<ProgramNode<T, Reduceable<T>>>
 */
fun <T> generateProgram(
    functions: List<Fun<T>>,
    terminals: List<Terminal<T>>,
    min: Int,
    max: Int,
    condition: (Int, Int) -> Boolean
): List<ProgramNode<T, Reduceable<T>>> {
    val program = mutableListOf<ProgramNode<T, Reduceable<T>>>()
    val height = Core.random.nextInt(min, max)
    val depths = mutableListOf(0)
    while (depths.isNotEmpty()) {
        val depth = depths.removeLast()
        val node = if (condition(height, depth)) {
            ProgramNode(terminals.random(Core.random))
        } else {
            ProgramNode(functions.random(Core.random)).also { node ->
                depths.addAll(List(node.arity) { depth + 1 })
            }
        }
        program.add(node)
    }
    return program
}
