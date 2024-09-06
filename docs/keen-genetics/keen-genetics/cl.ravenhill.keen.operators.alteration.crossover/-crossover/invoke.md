//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[Crossover](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun &lt;[S](invoke.md) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [S](invoke.md)&gt;

Performs the crossover operation on the given evolutionary state.

This method is the primary entry point for executing a crossover operation within an evolutionary algorithm. It selects parents from the population according to the defined parameters (number of parents, exclusivity) and generates offspring through the crossover process. The resulting population is then used to build the next state of the evolutionary process.

## Error Handling:

The method catches and handles any exceptions that occur during the crossover operation, returning a [CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md) if the process fails.

#### Return

A result containing the new evolutionary state or a [CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md) if the operation fails.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state. |
| outputSize | The desired size of the output population after the crossover operation. |
| buildState | A function to build the new evolutionary state from the recombined individuals. |
