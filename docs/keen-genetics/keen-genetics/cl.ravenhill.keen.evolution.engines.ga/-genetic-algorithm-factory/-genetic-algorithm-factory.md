//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithmFactory](index.md)/[GeneticAlgorithmFactory](-genetic-algorithm-factory.md)

# GeneticAlgorithmFactory

[common]\
constructor(fitnessFunction: ([Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), genotypeFactory: [GenotypeFactory](../../cl.ravenhill.keen.genetics.genotype/-genotype-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;, initialState: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;? = null)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
