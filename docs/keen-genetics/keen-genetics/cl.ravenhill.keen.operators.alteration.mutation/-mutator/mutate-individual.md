//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[Mutator](index.md)/[mutateIndividual](mutate-individual.md)

# mutateIndividual

[common]\
open fun [mutateIndividual](mutate-individual.md)(individual: [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [Individual](../../../../keen-core/keen-core/cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Mutates an individual by applying mutation to its chromosomes.

This function is used internally by the `invoke` function to mutate an individual. The mutation is applied to each chromosome of the individual based on the `chromosomeRate`.

#### Return

The mutated individual.

#### Parameters

common

| | |
|---|---|
| individual | The individual to mutate. |
