//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)/[map](map.md)

# map

[common]\
open override fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Chromosome](index.md)&lt;[T](index.md), [G](index.md)&gt;

Transforms the genes in the chromosome by applying a given function to each gene's value.

The `map` function creates a new chromosome by applying the provided transformation function to each gene in the current chromosome. It produces a new chromosome instance with the transformed genes, preserving the structure of the original chromosome. This function is typically used to apply a uniform operation to each gene, such as scaling, shifting, or otherwise modifying the gene values.

#### Return

A new chromosome with the transformed gene values.

#### Parameters

common

| | |
|---|---|
| transform | The transformation function to apply to each gene's value. |
