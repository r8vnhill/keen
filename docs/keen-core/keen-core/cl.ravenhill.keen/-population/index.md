//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[Population](index.md)

# Population

typealias [Population](index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Represents a population of individuals in an evolutionary algorithm.

The `Population` type alias defines a population as a list of individuals. This type alias simplifies the usage of populations within evolutionary algorithms by providing a clear and concise way to represent collections of individuals.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
