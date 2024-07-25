/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.annotations.ExperimentalKeen
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Terminal
import cl.ravenhill.keen.utils.trees.Tree
import cl.ravenhill.keen.utils.trees.generate

/**
 * A typealias for a function signature that represents a method to generate [Program]<T> instances.
 * `GenerationMethod<T>` is part of the experimental features in the Keen library and defines a specific format
 * for functions that create program trees with given parameters.
 *
 * @param T The type of the values processed by the program nodes.
 */
@ExperimentalKeen
typealias GenerationMethod<T> = (
    terminals: List<Terminal<T>>,
    functions: List<Fun<T>>,
    min: Int,
    max: Int,
) -> Program<T>

/**
 * Generates a `Program<T>` instance using one of several provided generation methods. This function is part of the
 * experimental features in the Keen library and allows for flexible program tree creation by choosing randomly from
 * multiple generation strategies.
 *
 * ## Constraints:
 * - Validates that the list of generation methods is not empty to ensure that a generation method is available for
 *   selection.
 *
 * ## Usage:
 * This method is particularly useful in scenarios where varying strategies for program generation are desirable, such
 * as in genetic programming or when experimenting with different tree generation techniques.
 *
 * ### Example:
 * ```
 * val generationMethods: List<GenerationMethod<MyValueType>> = listOf(
 *     ::generateProgramFull,
 *     ::generateProgramGrowing
 * )
 * val program = generateProgramWith(
 *     methods = generationMethods,
 *     terminals = listOfTerminals,
 *     functions = listOfFunctions,
 *     min = 2,
 *     max = 5
 * )
 * // program is a randomly generated Program<MyValueType> instance using one of the specified generation methods
 * ```
 *
 * @param methods A list of `GenerationMethod<T>` instances to choose from for program generation.
 * @param terminals A list of `Terminal<T>` nodes to be used in the program tree.
 * @param functions A list of `Fun<T>` nodes to be used as intermediate nodes in the program tree.
 * @param min The minimum height of the program tree to be generated.
 * @param max The maximum height of the program tree to be generated.
 * @param T The type of the values processed by the program nodes.
 * @return A `Program<T>` instance generated using one of the provided methods.
 */
@ExperimentalKeen
fun <T> generateProgramWith(
    methods: List<GenerationMethod<T>>,
    terminals: List<Terminal<T>>,
    functions: List<Fun<T>>,
    min: Int,
    max: Int,
): Program<T> {
    constraints { "The list of generation methods must not be empty" { methods mustNot BeEmpty } }
    return methods.random(Domain.random).invoke(terminals, functions, min, max)
}

/**
 * Generates a [Program] structure with a growing tree configuration within specified height constraints, using terminal
 * and function nodes. This function extends the [generateProgram] function, focusing on creating growing program trees.
 * It is marked as experimental in the Keen library.
 *
 * ## Functionality:
 * - Generates a [Program] tree where the decision to create terminal or function nodes is probabilistically based on
 *   their relative sizes in the provided lists.
 * - Utilizes the [generateProgram] method, providing it with a condition that allows for a mix of full and partial
 *   expansion of the tree, depending on the depth and a random factor.
 * - The probability of creating a terminal node increases as the depth approaches the minimum height or based on the
 *   ratio of terminal to function nodes.
 * - The tree's height is determined randomly within the specified minimum (`min`) and maximum (`max`) bounds.
 *
 * ## Usage:
 * This method is useful for generating program trees that are not strictly full or balanced, offering a more varied
 * and natural structure. This is particularly beneficial in genetic programming or simulations where diversity in tree
 * structures is desired.
 *
 * ### Example:
 * ```
 * val growingProgram = generateProgramGrowing(
 *     terminals = listOfTerminalNodes,
 *     functions = listOfFunctionNodes,
 *     min = 2,
 *     max = 5
 * )
 * // growingProgram is a randomly generated Program instance with a growing tree structure and height between 2 and 5
 * ```
 *
 * @param terminals A list of `Terminal<T>` nodes to be used in the program tree.
 * @param functions A list of `Fun<T>` nodes to be used as intermediate nodes in the program tree.
 * @param min The minimum height of the program tree to be generated.
 * @param max The maximum height of the program tree to be generated.
 * @param T The type of the values processed by the program nodes.
 * @return A newly generated `Program<T>` instance with a growing tree structure within the specified height
 *   constraints.
 */
@ExperimentalKeen
fun <T> generateProgramGrowing(
    terminals: List<Terminal<T>>,
    functions: List<Fun<T>>,
    min: Int,
    max: Int,
) = generateProgram(terminals, functions, min, max) { h, d ->
    d == h || d >= min && Domain.random.nextDouble() < terminals.size / (terminals.size + functions.size).toDouble()
}

/**
 * Generates a [Program] structure with a full tree configuration within specified height constraints, using terminal
 * and function nodes. This function is an extension of the [generateProgram] function, specifically tailored to create
 * full program trees. It is marked as experimental in the Keen library.
 *
 * ## Functionality:
 * - Generates a full `Program` tree, ensuring that every leaf node is at the maximum depth of the tree.
 * - Utilizes the [generateProgram] method, providing it with a condition that ensures the tree is 'full' — meaning
 *   each non-leaf node expands until the maximum height is reached.
 * - The tree's height is determined randomly within the specified minimum ([min]) and maximum ([max]) bounds.
 *
 * ## Usage:
 * This method is particularly useful in scenarios where a complete and balanced program tree structure is required,
 * such as in certain types of genetic programming or in algorithmic simulations where full tree exploration is
 * important.
 *
 * ### Example:
 * ```
 * val fullProgram = generateProgramFull(
 *     terminals = listOfTerminalNodes,
 *     functions = listOfFunctionNodes,
 *     min = 2,
 *     max = 5
 * )
 * // fullProgram is a randomly generated full Program instance with height between 2 and 5
 * ```
 *
 * @param terminals A list of [Terminal]<[T]> nodes to be used in the program tree.
 * @param functions A list of [Fun]<[T]> nodes to be used as intermediate nodes in the program tree.
 * @param min The minimum height of the program tree to be generated.
 * @param max The maximum height of the program tree to be generated.
 * @param T The type of the values processed by the program nodes.
 * @return A newly generated [Program]<[T]> instance with a full tree structure within the specified height constraints.
 */
@ExperimentalKeen
fun <T> generateProgramFull(
    terminals: List<Terminal<T>>, functions: List<Fun<T>>, min: Int, max: Int,
) = generateProgram(terminals, functions, min, max) { h, d -> h == d }

/**
 * Generates a random `Program` structure with specified height constraints using terminal and function nodes. This
 * function is part of the experimental features in the Keen library, tailored for constructing program trees in
 * computational models such as genetic programming.
 *
 * ## Functionality:
 * - Generates a `Program` tree using lists of terminal ([Terminal]<[T]>) and function ([Fun]<[T]>) nodes.
 * - The height of the generated program is determined randomly within the specified minimum ([min]) and maximum ([max])
 *   bounds.
 * - A [condition] function is used to guide the decision of creating terminal or function nodes at each level.
 * - Utilizes the [Tree.Companion.generate] method to construct the program tree, providing leaf and intermediate
 *   factories for program nodes.
 *
 * ## Usage:
 * This method is particularly useful for generating random program structures in genetic programming, algorithm
 * testing, and simulations where controlled variability in program complexity is required.
 *
 * ### Example:
 * ```
 * val program = generateProgram(
 *     terminals = listOfTerminalNodes,
 *     functions = listOfFunctionNodes,
 *     min = 2,
 *     max = 5,
 *     condition = { maxHeight, depth -> depth >= maxHeight }
 * )
 * // program is a randomly generated Program instance with height between 2 and 5
 * ```
 *
 * @param terminals A list of `Terminal<T>` nodes to be used in the program tree.
 * @param functions A list of `Fun<T>` nodes to be used as intermediate nodes in the program tree.
 * @param min The minimum height of the program tree to be generated.
 * @param max The maximum height of the program tree to be generated.
 * @param condition A function that determines whether to create a terminal or function node.
 * @param T The type of the values processed by the program nodes.
 * @return A newly generated `Program<T>` instance within the specified height constraints.
 */
@ExperimentalKeen
fun <T> generateProgram(
    terminals: List<Terminal<T>>,
    functions: List<Fun<T>>,
    min: Int,
    max: Int,
    condition: (Int, Int) -> Boolean,
) = Tree.generate(
    nodes = terminals to functions,
    heightRange = min..max,
    condition = condition,
    leafFactory = { leaf -> Program(leaf) },
    intermediateFactory = { intermediate, children -> Program(intermediate, children) },
)
