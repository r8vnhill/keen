//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[BitFlipMutator](index.md)/[mutateGene](mutate-gene.md)

# mutateGene

[common]\
open override fun [mutateGene](mutate-gene.md)(gene: [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)): [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)

Performs a bit-flip mutation on a single boolean gene.

This method inverts the value of the given boolean gene—changing `true` to `false` or `false` to `true`. The mutation is applied based on the mutation rates configured for the individual, chromosome, and gene levels.

#### Return

A new boolean gene with its value flipped.

#### Parameters

common

| | |
|---|---|
| gene | The boolean gene to be mutated. |
