//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[Genotype](index.md)/[map](map.md)

# map

[common]\
open override fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Genotype](index.md)&lt;[T](index.md), [G](index.md)&gt;

Applies a transformation function to each gene in the genotype, producing a new genotype with the transformed genes.

The `map` function transforms the values of all genes across all chromosomes in the genotype using the specified transformation function [transform](map.md). A new `Genotype` instance is returned, with each gene in each chromosome replaced by the result of applying the transformation function to the original gene's value. This is a non-mutating operation that produces a new genotype, leaving the original genotype unchanged.

#### Return

A new `Genotype` instance where each gene's value has been transformed by the [transform](map.md) function.

#### Parameters

common

| | |
|---|---|
| transform | The transformation function to apply to each gene's value. |
