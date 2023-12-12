package cl.ravenhill.keen.prog

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.utils.trees.Tree
import java.util.Objects


/**
 * Represents a program structure in the form of a tree where each node is a reducible expression. The `Program` class
 * extends the [Tree] interface, enabling it to function as a node within a larger program tree. This class is part of
 * the experimental features in the Keen library and is tailored for encapsulating computational logic in a tree-like
 * structure.
 *
 * ## Constraints:
 * - The arity of the reducible expression ([Reducible.arity]) must equal the number of children. This ensures the
 *   structural integrity and correctness of the program tree.
 *
 * ## Usage:
 * The `Program` class is instrumental in representing and manipulating structured programs in computational models,
 * such as genetic programming or complex expression trees. It facilitates the creation of hierarchical, tree-based
 * computational structures.
 *
 * ### Example:
 * ```
 * @OptIn(ExperimentalKeen::class)
 * val program = Program(reducibleExpression, listOf(childProgram1, childProgram2))
 * val result = program(environment, arg1, arg2)
 * // result holds the evaluation outcome of the program
 * ```
 *
 * @param V The type of values processed by the reducible expressions in the program.
 * @param value The reducible expression at this node of the program.
 * @param children The child nodes of this program, each a `Program` instance.
 * @property arity The arity (number of arguments) of the reducible expression.
 * @property nodes A depth-first list of all program nodes in the tree, starting from the current node.
 * @property root An alias for the first node in the [nodes] list.
 * @constructor Creates a new `Program` instance, ensuring that the number of children matches the arity of the
 *   provided reducible expression.
 */
@ExperimentalKeen
class Program<V>(
    override val value: Reducible<V>,
    override val children: List<Program<V>> = emptyList()
) : Tree<Reducible<V>, Program<V>> {

    init {
        constraints {
            "The arity of the reducible expression must be equal to the number of children" {
                children must HaveSize(value.arity)
            }
        }
    }

    override val arity: Int = value.arity

    override val nodes: List<Program<V>> by lazy { listOf(this) + children.flatMap { it.nodes } }

    val root: Program<V> = nodes.first()

    /**
     * Creates and returns a new instance of the `Program` class with a specified reducible expression and list of
     * children.
     *
     * This method overrides the `createNode` method from the `Tree` interface, providing a way to construct new
     * `Program` nodes with given values and children. It utilizes the [copy] function to facilitate the creation of
     * the new node.
     *
     * ## Functionality:
     * - Constructs a new `Program` instance that encapsulates the provided reducible expression (`value`) and the
     *   specified list of children (`children`).
     *
     * ## Usage:
     * The `createNode` method is particularly useful in scenarios involving tree manipulation, such as in genetic
     * programming, where new program trees or subtrees need to be constructed dynamically based on evolving conditions
     * or operations.
     *
     * ### Example:
     * ```
     * val program: Program<MyValueType> = ...
     * val newReducible: Reducible<MyValueType> = ...
     * val newChildren: List<Program<MyValueType>> = ...
     * val newNode = program.createNode(newReducible, newChildren)
     * // newNode is now a new Program instance with the specified value and children
     * ```
     *
     * @param value The reducible expression to be used as the value of the new node.
     * @param children The list of child `Program` instances to be attached to the new node.
     * @return A new `Program` instance with the given value and children.
     */
    override fun createNode(value: Reducible<V>, children: List<Program<V>>) = copy(value, children)

    /**
     * Evaluates the program by invoking its reducible expression with a specified environment and a set of arguments.
     *
     * This operator function enables the `Program` class to act as an executable entity within a computational context,
     * following the principles of functional programming.
     *
     * ## Functionality:
     * - Applies the reducible expression (`value`) of the program to the given environment and arguments.
     * - For each child in the `children` list, recursively invokes the child program with the same environment and
     *   arguments.
     * - The results of these child invocations are passed as a list of arguments to the parent program's reducible
     *   expression.
     * - Returns the result of evaluating the reducible expression with these arguments.
     *
     * ## Usage:
     * This operator function is critical in scenarios where the program needs to be executed or evaluated,
     * particularly in computational models such as genetic programming, or complex expression evaluations. It allows
     * for the dynamic execution of hierarchical program structures.
     *
     * ### Example:
     * ```
     * val program: Program<MyValueType> = ...
     * val environment: Environment<MyValueType> = ...
     * val result = program(environment, arg1, arg2, arg3)
     * // result holds the outcome of evaluating the program with the provided arguments
     * ```
     *
     * @param environment The `Environment<V>` in which the program operates.
     * @param args A variable number of arguments of type `V` used in the program's evaluation.
     * @return The result of evaluating the program's reducible expression with the given environment and arguments.
     */
    operator fun invoke(environment: Environment<V>, vararg args: V): V =
        value(environment, children.map { it(environment, *args) })

    /**
     * Creates a copy of the current `Program` instance with optional new value and children.
     *
     * This method provides a way to create a modified version of the program, allowing for adjustments in its
     * reducible expression or its children while retaining the overall structure.
     *
     * ## Functionality:
     * - Allows for specifying a new `Reducible<V>` value and/or a new list of children `Program<V>` instances.
     * - If no new value or children are provided, the method uses the current program's value and children.
     * - Returns a new `Program` instance with the specified or inherited value and children.
     *
     * ## Usage:
     * The `copy` method is particularly useful in scenarios involving program manipulation or transformation, where
     * modifications to parts of the program are required while maintaining the integrity of the rest of the structure.
     * It is a common pattern in immutable or persistent data structures.
     *
     * ### Example:
     * ```
     * val originalProgram: Program<MyValueType> = ...
     * val modifiedProgram = originalProgram.copy(
     *     value = newReducibleExpression,
     *     children = newChildrenList
     * )
     * // modifiedProgram is now a new Program instance with updated value and children
     * ```
     *
     * @param value The new reducible expression for the program; defaults to the current program's value if not
     *   specified.
     * @param children The new list of child programs; defaults to the current program's children if not specified.
     * @return A new `Program` instance with the specified or current value and children.
     */
    fun copy(value: Reducible<V> = this.value, children: List<Program<V>> = this.children) = Program(value, children)

    /**
     * Generates a simple string representation of the program. This method overrides the `toSimpleString` method from
     * the `MultiStringFormat` interface and provides a concise view of the program's structure, focusing on the
     * reducible expression and its immediate children.
     *
     * ## Functionality:
     * - Constructs a string representation of the program based on its current state.
     *     - If the program has no children, it returns the string representation of the program's value (reducible
     *       expression).
     *     - If the program has children, it formats the string to include the reducible expression followed by a
     *       space-separated list of its children's string representations, all enclosed in parentheses.
     *
     * ## Usage:
     * This method is useful for debugging, logging, or displaying the program in a human-readable format. It
     * simplifies the visualization of the program's structure, especially in scenarios where a quick overview of the
     * program is needed.
     *
     * ### Example:
     * ```
     * val program: Program<MyValueType> = ...
     * val simpleString = program.toSimpleString()
     * // If program has no children, simpleString might be "MyValue"
     * // If program has children, simpleString might be "(MyValue child1 child2 child3)"
     * ```
     *
     * @return A string representation of the program, concisely showing its value and immediate children.
     */
    override fun toSimpleString() = when {
        children.isEmpty() -> value.toString()
        else -> "(${value.toSimpleString()} ${children.joinToString(" ") { it.toString() }})"
    }

    /**
     * Provides a detailed string representation of the `Program` instance. This method overrides the `toString` method
     * from the `Object` class and offers a more informative view of the program, including its value and the values of
     * its immediate children.
     *
     * ## Functionality:
     * - Constructs a detailed string representation of the program.
     * - Includes the program's reducible expression (`value`) and a list of the values of its children.
     * - The children's values are presented in a list format for clarity.
     *
     * ## Usage:
     * This method is particularly useful for obtaining a comprehensive textual representation of the program, which can
     * be helpful in debugging, logging, or displaying the program structure in a detailed manner.
     *
     * ### Example:
     * ```
     * val program: Program<MyValueType> = ...
     * val programString = program.toString()
     * // programString might be "Program(value=MyValue, children=[ChildValue1, ChildValue2, ChildValue3])"
     * ```
     *
     * @return A string representation of the program, including its value and the values of its children.
     */
    override fun toString() = "Program(value=$value, children=${children.map { it.value }})"

    /**
     * Generates a comprehensive string representation of the `Program` instance, providing detailed information about
     * the program's structure. This method overrides the `toDetailedString` method from the `MultiStringFormat`
     * interface, offering an in-depth view of the program, including its value and the detailed descriptions of its
     * children.
     *
     * ## Functionality:
     * - Utilizes a `StringBuilder` to construct a detailed string representation.
     * - Starts with the program's basic information, including its reducible expression (`value`) and the list of
     *   children.
     * - Adds a detailed description of the program's own reducible expression.
     * - Iterates through each child, appending a detailed description of each child's value to the string.
     * - Returns the complete string constructed, providing a thorough overview of the program and its children.
     *
     * ## Usage:
     * This method is especially useful for debugging and analysis purposes, where a detailed insight into the program's
     * structure and its components is required. It aids in understanding the intricate details of the program and its
     * sub-components.
     *
     * ### Example:
     * ```
     * val program: Program<MyValueType> = ...
     * val detailedString = program.toDetailedString()
     * // detailedString might look like:
     * // "Program(value=MyValue, children=[Child1, Child2])\n  MyValue(detailed)\n  ChildValue1(detailed)\n
     * // ChildValue2(detailed)\n"
     * ```
     *
     * @return A detailed string representation of the program, including its value and a detailed description of its
     *   children.
     */
    override fun toDetailedString(): String {
        val sb = StringBuilder()
        sb.append("Program(value=$value, children=$children)\n")
        sb.append("  ${value.toDetailedString()}\n")
        children.forEach { child ->
            sb.append("  ${child.value.toDetailedString()}\n")
        }
        return sb.toString()
    }

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Program<*> -> false
        value != other.value -> false
        children != other.children -> false
        else -> false
    }

    override fun hashCode() = Objects.hash(Program::class, value, children)

    companion object {
        /**
         * The default maximum depth for program trees, set to 7. This value is used to prevent the creation of overly
         * complex and computationally expensive programs.
         *
         * @See [Domain.maxProgramDepth]
         */
        internal const val DEFAULT_MAX_DEPTH = 7
    }
}
