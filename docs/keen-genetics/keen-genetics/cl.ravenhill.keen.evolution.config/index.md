//[keen-genetics](../../index.md)/[cl.ravenhill.keen.evolution.config](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AlterationConfiguration](-alteration-configuration/index.md) | [common]<br>data class [AlterationConfiguration](-alteration-configuration/index.md)&lt;[T](-alteration-configuration/index.md), [G](-alteration-configuration/index.md) : [Gene](../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](-alteration-configuration/index.md), [G](-alteration-configuration/index.md)&gt;&gt;(val alterers: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Alterer](../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](-alteration-configuration/index.md), [G](-alteration-configuration/index.md), [Genotype](../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](-alteration-configuration/index.md), [G](-alteration-configuration/index.md)&gt;&gt;&gt;) |
| [GeneticPopulationConfiguration](-genetic-population-configuration/index.md) | [common]<br>data class [GeneticPopulationConfiguration](-genetic-population-configuration/index.md)&lt;[T](-genetic-population-configuration/index.md), [G](-genetic-population-configuration/index.md) : [Gene](../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](-genetic-population-configuration/index.md), [G](-genetic-population-configuration/index.md)&gt;&gt;(val genotypeFactory: [GenotypeFactory](../cl.ravenhill.keen.genetics/-genotype-factory/index.md)&lt;[T](-genetic-population-configuration/index.md), [G](-genetic-population-configuration/index.md)&gt;, val populationSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
