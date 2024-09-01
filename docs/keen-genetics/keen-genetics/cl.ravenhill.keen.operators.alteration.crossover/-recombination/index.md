//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[Recombination](index.md)

# Recombination

typealias [Recombination](index.md)&lt;[T](index.md), [G](index.md)&gt; = [Crossover](../-crossover/index.md)&lt;[T](index.md), [G](index.md)&gt;

Type alias for the `Crossover` interface, representing recombination operations in genetic algorithms.

The `Recombination` alias is used interchangeably with the [Crossover](../-crossover/index.md) interface to emphasize the role of crossover operations as a form of recombination in genetic algorithms. Recombination refers to the process of combining genetic material from multiple parent individuals to create offspring, thereby introducing genetic diversity within the population.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### See also

| |
|---|
| [Crossover](../-crossover/index.md) |
