package cl.ravenhill.keen.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.requirements.CollectionRequirement.NotBeEmpty
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.util.trees.Tree
import cl.ravenhill.keen.util.trees.generateRecursive

/***************************************************************************************************
 * This code defines functions to generate random programs in the form of breadth-first trees given
 * a list of functions and terminals.
 **************************************************************************************************/

/**
 * Typealias for a generation method that takes a list of terminals, a list of functions,
 * a minimum program length, and a maximum program length, and returns a program.
 */
typealias GenerationMethod<T> = (
    terminals: List<Terminal<T>>, functions: List<Fun<T>>, min: Int, max: Int
) -> Program<T>


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
    methods: List<GenerationMethod<T>>,
    terminals: List<Terminal<T>>,
    functions: List<Fun<T>>,
    min: Int,
    max: Int
): Program<T> {
    enforce { "The list of generation methods must not be empty" { methods must NotBeEmpty } }
    return methods.random(Core.random).invoke(terminals, functions, min, max)
}

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
    val condition = { h: Int, d: Int ->
        d == h || d >= min && Dice.probability() < terminals.size.toDouble() / (terminals.size + functions.size)
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
    enforce {
        "The list of terminals and functions must not be empty" {
            terminals.size + functions.size must BePositive
        }
        "The minimum height must be positive" { min must BePositive }
        "The maximum height must be positive" { max must BePositive }
        "The maximum height must be greater than the minimum height" { max must BeAtLeast(min) }
    }
    val height = Core.random.nextInt(min, max)
    return generateProgramRecursive(functions, terminals, height, condition)
}

/**
 * Generates a random program recursively.
 * The tree is built from the root to the leaves, and it stops growing the current branch when
 * the condition is met; in which case, it backtracks to the root and starts a new branch.
 *
 * @param functions the list of functions.
 * @param terminals the list of terminals.
 * @param height the maximum height of the program tree.
 * @param condition the condition to stop growing the current branch.
 *
 * @return a random program as a depth-first tree.
 *
 * @see Tree.Companion.generateRecursive
 */
private fun <T> generateProgramRecursive(
    functions: List<Fun<T>>,
    terminals: List<Terminal<T>>,
    height: Int,
    condition: (Int, Int) -> Boolean,
) = Tree.generateRecursive(functions, terminals, 0, height, condition, ::Program) { f, c ->
    Program(f, c)
}