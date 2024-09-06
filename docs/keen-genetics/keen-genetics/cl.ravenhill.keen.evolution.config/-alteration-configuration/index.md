//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.config](../index.md)/[AlterationConfiguration](index.md)

# AlterationConfiguration

data class [AlterationConfiguration](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(val alterers: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;)

Configuration for applying alterations to genotypes in an evolutionary algorithm.

The `AlterationConfiguration` class encapsulates a list of [Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md) instances, which are responsible for modifying genotypes during the evolutionary process. Alterers can perform various genetic operations, such as mutation, crossover, or any other genetic modification. This configuration is used to apply these alterations in the evolutionary cycle.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the genotype. |
| G | The type of gene within the genotype, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

## Constructors

| | |
|---|---|
| [AlterationConfiguration](-alteration-configuration.md) | [common]<br>constructor(alterers: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;) |

## Properties

| Name | Summary |
|---|---|
| [alterers](alterers.md) | [common]<br>val [alterers](alterers.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;<br>The list of [Alterer](../../../../keen-core/keen-core/cl.ravenhill.keen.operators.alteration/-alterer/index.md) instances responsible for modifying the genotypes. |
