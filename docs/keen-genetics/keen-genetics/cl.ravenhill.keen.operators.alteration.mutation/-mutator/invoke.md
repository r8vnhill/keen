//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[Mutator](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun &lt;[S](invoke.md) : [EvolutionState](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[MutationException](../../cl.ravenhill.keen.exceptions/-mutation-exception/index.md), [S](invoke.md)&gt;

Mutates a population of individuals, creating a new evolutionary state.

This function applies the mutation process to a population, generating a new evolutionary state. The mutation is applied based on the `individualRate` and `chromosomeRate`. If the `individualRate` is 0.0, no mutation occurs, and the original state is returned.

#### Return

The new evolutionary state after mutation, or an error if the output size is invalid.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state. |
| outputSize | The expected size of the output population. This must match the current population size. |
| buildState | A function to build the new evolutionary state from the mutated individuals. |

#### Throws

| | |
|---|---|
| [MutationException](../../cl.ravenhill.keen.exceptions/-mutation-exception/index.md) | if the output size is invalid or if an error occurs during mutation. |
