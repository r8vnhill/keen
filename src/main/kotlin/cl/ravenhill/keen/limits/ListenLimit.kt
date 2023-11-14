/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.EvolutionListener

/**
 * Defines a conditional stopping criterion for an evolutionary process, based on a custom predicate.
 * The `ListenLimit` evaluates a given condition against the current state of the evolutionary engine,
 * facilitating dynamic and flexible termination criteria.
 *
 * This limit utilizes a lambda function as a predicate, which operates on an [EvolutionListener] instance.
 * When the predicate's condition is met, it signals the evolutionary engine to halt the process.
 *
 * @param DNA The type of genetic material being evolved.
 * @param G The type of gene within the genetic material.
 * @property listener The [EvolutionListener] that monitors the evolutionary process.
 * @property predicate A lambda function acting on [EvolutionListener]. It defines the stopping criterion based on the
 *                     state of the evolution. Returns `true` to signal the end of the evolutionary process.
 *
 * @constructor Initializes a `ListenLimit` with a specified [listener] and [predicate].
 *
 * @see Engine The evolution engine to which this limit applies.
 * @see EvolutionListener A listener interface for monitoring and reacting to the evolution process.
 *
 * ## Example
 * ### Defining a Custom Listen Limit:
 * ```
 * val myListener = object : EvolutionListener<MyDNA, MyGene> {
 *     // implementation of listener methods
 * }
 * val myLimit = ListenLimit(myListener) {
 *     // Define a condition based on the state of `myListener`
 *     this.someCondition()
 * }
 * ```
 * In this example, `myLimit` uses `myListener` to evaluate the custom condition `someCondition()`.
 * When this condition returns `true`, the evolutionary engine will stop.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 1.0.0
 * @version 2.0.0
 */
open class ListenLimit<DNA, G>(
    val listener: EvolutionListener<DNA, G>,
    private val predicate: EvolutionListener<DNA, G>.() -> Boolean
) : Limit<DNA, G> where G : Gene<DNA, G> {
    override var engine: Evolver<DNA, G>? = null
        set(value) {
            value?.listeners?.add(listener)
            field = value
        }

    /**
     * Evaluates the custom predicate on the assigned [listener] to determine if the evolutionary process should stop.
     *
     * @return `true` if the predicate condition is met, signaling the end of evolution; otherwise `false`.
     */
    override fun invoke(): Boolean = predicate(listener)
}
