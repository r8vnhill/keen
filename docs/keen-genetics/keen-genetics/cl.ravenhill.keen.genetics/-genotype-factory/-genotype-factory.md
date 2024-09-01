//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics](../index.md)/[GenotypeFactory](index.md)/[GenotypeFactory](-genotype-factory.md)

# GenotypeFactory

[common]\
constructor(executor: [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; = Domain.defaultConstructor())

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the chromosomes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| executor | The `ConstructorExecutor` used to generate the chromosomes for the genotype. Defaults to `CoroutineConcurrentConstructor`. |
