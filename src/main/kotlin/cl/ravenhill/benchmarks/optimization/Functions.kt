/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.benchmarks.optimization

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import kotlin.math.*

fun ackley(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -20 * exp(-0.2 * sqrt(0.5 * (x.pow(2) + y.pow(2)))) -
            exp(0.5 * (cos(2 * PI * x) + cos(2 * PI * y))) + exp(1.0) + 20.0
}

fun beale(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    (1.5 - x + x * y).pow(2) + (2.25 - x + x * y.pow(2)).pow(2) +
            (2.625 - x + x * y.pow(3)).pow(2)
}

fun booth(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    (x + 2 * y - 7).pow(2) + (2 * x + y - 5).pow(2)
}

fun bukinN6(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    100 * (y - 0.01 * x.pow(2) + 1).pow(2) + 0.01 * (x + 10).pow(2)
}

fun crossInTray(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -0.0001 * (abs(sin(x) * sin(y) * exp(abs(100 - sqrt(x.pow(2) + y.pow(2)) / PI))) + 1).pow(0.1)
}

fun easom(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -cos(x) * cos(y) * exp(-(x - PI).pow(2) - (y - PI).pow(2))
}

fun eggholder(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -(y + 47) * sin(sqrt(abs(x / 2 + (y + 47)))) - x * sin(sqrt(abs(x - (y + 47))))
}

fun goldsteinPrice(g: Genotype<Double, DoubleGene>) = g.flatMap().let { (x, y) ->
    (1 + (x + y + 1).pow(2) * (19 - 14 * x + 3 * x.pow(2) - 14 * y + 6 * x * y + 3 * y.pow(2))) *
            (30 + (2 * x - 3 * y).pow(2) * (18 - 32 * x + 12 * x.pow(2) + 48 * y - 36 * x * y + 27 * y.pow(2)))
}

fun himmelblau(g: Genotype<Double, DoubleGene>) = g.flatMap().let { (x, y) ->
    (x.pow(2) + y - 11).pow(2) + (x + y.pow(2) - 7).pow(2)
}

fun holderTable(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    -abs(sin(x) * cos(y) * exp(abs(1 - sqrt(x.pow(2) + y.pow(2)) / PI)))
}

fun levi(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    sin(3 * PI * x).pow(2) + (x - 1).pow(2) * (1 + sin(3 * PI * y).pow(2)) +
            (y - 1).pow(2) * (1 + sin(2 * PI * y).pow(2))
}

fun matyas(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    0.26 * (x.pow(2) + y.pow(2)) - 0.48 * x * y
}

fun mccormick(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    sin(x + y) + (x - y).pow(2) - 1.5 * x + 2.5 * y + 1
}

fun rastrigin(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    20 + x.pow(2) - 10 * cos(2 * PI * x) + y.pow(2) - 10 * cos(2 * PI * y)
}

fun rosenbrock(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    100 * (y - x.pow(2)).pow(2) + (1 - x).pow(2)
}

fun schafferN2(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    0.5 + (sin(x.pow(2) - y.pow(2)).pow(2) - 0.5) / (1 + 0.001 * (x.pow(2) + y.pow(2))).pow(2)
}

fun schafferN4(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    0.5 + (cos(sin(abs(x.pow(2) - y.pow(2)))).pow(2) - 0.5) / (1 + 0.001 * (x.pow(2) + y.pow(2))).pow(2)
}

fun sphere(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    x.pow(2) + y.pow(2)
}

fun styblinskiTang(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    0.5 * (x.pow(4) - 16 * x.pow(2) + 5 * x + y.pow(4) - 16 * y.pow(2) + 5 * y)
}

fun threeHumpCamel(genotype: Genotype<Double, DoubleGene>) = genotype.flatMap().let { (x, y) ->
    2 * x.pow(2) - 1.05 * x.pow(4) + x.pow(6) / 6 + x * y + y.pow(2)
}

