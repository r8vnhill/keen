//[keen-core](../../index.md)/[cl.ravenhill.keen](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Domain](-domain/index.md) | [common]<br>object [Domain](-domain/index.md)<br>A singleton object that encapsulates global configuration and settings for the evolutionary algorithm domain. |
| [Individual](-individual/index.md) | [common]<br>data class [Individual](-individual/index.md)&lt;[T](-individual/index.md), [F](-individual/index.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](-individual/index.md), [F](-individual/index.md)&gt;, [R](-individual/index.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](-individual/index.md), [F](-individual/index.md)&gt;&gt;(val representation: [R](-individual/index.md), val fitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = Double.NaN) : [Verifiable](../cl.ravenhill.keen.mixins/-verifiable/index.md), [FlatMappable](../cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](-individual/index.md)&gt; , [Foldable](../cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](-individual/index.md)&gt; <br>Represents an individual in an evolutionary algorithm. |
| [Population](-population/index.md) | [common]<br>typealias [Population](-population/index.md)&lt;[T](-population/index.md), [F](-population/index.md), [R](-population/index.md)&gt; = [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](-individual/index.md)&lt;[T](-population/index.md), [F](-population/index.md), [R](-population/index.md)&gt;&gt;<br>Represents a population of individuals in an evolutionary algorithm. |
| [ToStringMode](-to-string-mode/index.md) | [common]<br>enum [ToStringMode](-to-string-mode/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ToStringMode](-to-string-mode/index.md)&gt; <br>Enum representing the different modes for converting objects to their string representation. |

## Properties

| Name | Summary |
|---|---|
| [fitness](fitness.md) | [common]<br>val &lt;[T](fitness.md), [F](fitness.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](fitness.md), [F](fitness.md)&gt;, [R](fitness.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](fitness.md), [F](fitness.md)&gt;&gt; [Population](-population/index.md)&lt;[T](fitness.md), [F](fitness.md), [R](fitness.md)&gt;.[fitness](fitness.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Extension property to get the fitness values of the individuals in the population. |
