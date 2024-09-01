//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[Mutator](index.md)/[mutateChromosome](mutate-chromosome.md)

# mutateChromosome

[common]\
abstract fun [mutateChromosome](mutate-chromosome.md)(chromosome: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;): [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;

Mutates a chromosome, producing a new chromosome.

This function defines how a chromosome is mutated. The specific implementation of this function determines the type and extent of mutation applied to the chromosome's genes.

#### Return

The mutated chromosome.

#### Parameters

common

| | |
|---|---|
| chromosome | The chromosome to mutate. |
