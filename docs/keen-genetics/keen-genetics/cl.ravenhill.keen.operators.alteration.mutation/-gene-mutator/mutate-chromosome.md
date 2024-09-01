//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[GeneMutator](index.md)/[mutateChromosome](mutate-chromosome.md)

# mutateChromosome

[common]\
open override fun [mutateChromosome](mutate-chromosome.md)(chromosome: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;): [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;

Mutates the genes within a chromosome based on the gene mutation rate.

This method iterates over each gene in the chromosome and applies the mutation logic defined by [mutateGene](mutate-gene.md) if the random probability is less than the `geneRate`. The method returns a new chromosome with the potentially mutated genes, preserving the structure and integrity of the original chromosome.

#### Return

A new chromosome with mutated genes.

#### Parameters

common

| | |
|---|---|
| chromosome | The chromosome whose genes are to be mutated. |
