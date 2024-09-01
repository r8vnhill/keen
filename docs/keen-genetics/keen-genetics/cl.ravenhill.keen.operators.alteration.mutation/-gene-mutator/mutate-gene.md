//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[GeneMutator](index.md)/[mutateGene](mutate-gene.md)

# mutateGene

[common]\
abstract fun [mutateGene](mutate-gene.md)(gene: [G](index.md)): [G](index.md)

Defines the mutation logic for an individual gene.

Implementations of this interface must provide the specific logic for mutating a gene, determining how the gene's value or state is altered during the mutation process.

#### Return

The mutated gene.

#### Parameters

common

| | |
|---|---|
| gene | The gene to be mutated. |
