/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.benchmarks.optimization

import cl.ravenhill.keen.builders.chromosome
import cl.ravenhill.keen.builders.doubles
import cl.ravenhill.keen.builders.engine
import cl.ravenhill.keen.builders.genotype
import cl.ravenhill.keen.operators.mutator.RandomMutator
import cl.ravenhill.keen.operators.selector.RandomSelector

class Runner {
    fun run() {
        var warmup = true
        repeat(4) {
            val engine = engine(
                ::ackley,
                genotype {
                    chromosome {
                        doubles {
                            ranges += -5.0..5.0
                            size = 2
                        }
                    }
                }
            ) {
                populationSize = 500
                selector = RandomSelector()
                alterers = listOf(RandomMutator(0.06),)
            }
        }
    }
}

fun main() {
    Runner().run()
}
