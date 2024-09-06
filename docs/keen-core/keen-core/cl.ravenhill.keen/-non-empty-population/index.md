//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[NonEmptyPopulation](index.md)

# NonEmptyPopulation

class [NonEmptyPopulation](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [Population](../-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

A value class representing a non-empty population of individuals in an evolutionary algorithm.

The `NonEmptyPopulation` class is an inline class that ensures the population contains at least one individual. It implements the [PopulationLike](../-population-like/index.md) interface, providing access to the population's fitness values and inheriting the behavior of a list. This class is useful when an evolutionary algorithm requires a non-empty population for its operations.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

## Properties

| Name | Summary |
|---|---|
| [fitness](../-population-like/fitness.md) | [common]<br>open val [fitness](../-population-like/fitness.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Retrieves the fitness values of all individuals in the population. |
| [individuals](individuals.md) | [common]<br>open override val [individuals](individuals.md): NonEmptyList&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>A non-empty list of individuals in the population. |
| [size](index.md#844915858%2FProperties%2F1902964177) | [common]<br>open override val [size](index.md#844915858%2FProperties%2F1902964177): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [contains](index.md#-2081704821%2FFunctions%2F1902964177) | [common]<br>open operator override fun [contains](index.md#-2081704821%2FFunctions%2F1902964177)(element: [Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#-478735308%2FFunctions%2F1902964177) | [common]<br>open override fun [containsAll](index.md#-478735308%2FFunctions%2F1902964177)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [get](index.md#961975567%2FFunctions%2F1902964177) | [common]<br>open operator override fun [get](index.md#961975567%2FFunctions%2F1902964177)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [indexOf](index.md#2127718369%2FFunctions%2F1902964177) | [common]<br>open override fun [indexOf](index.md#2127718369%2FFunctions%2F1902964177)(element: [Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.md#-1000881820%2FFunctions%2F1902964177) | [common]<br>open override fun [isEmpty](index.md#-1000881820%2FFunctions%2F1902964177)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.md#-1577986619%2FFunctions%2F1902964177) | [common]<br>open operator override fun [iterator](index.md#-1577986619%2FFunctions%2F1902964177)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; |
| [lastIndexOf](index.md#-237563925%2FFunctions%2F1902964177) | [common]<br>open override fun [lastIndexOf](index.md#-237563925%2FFunctions%2F1902964177)(element: [Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](index.md#-236165689%2FFunctions%2F1902964177) | [common]<br>open override fun [listIterator](index.md#-236165689%2FFunctions%2F1902964177)(): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>open override fun [listIterator](index.md#845091493%2FFunctions%2F1902964177)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; |
| [map](../-population-like/map.md) | [common]<br>open fun [map](../-population-like/map.md)(f: ([Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) -&gt; [Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Population](../-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>Maps the individuals in the population using the provided function. |
| [plus](../-population/plus.md) | [common]<br>operator fun [plus](../-population/plus.md)(populationLike: [PopulationLike](../-population-like/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Population](../-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>Creates a new population by adding the individuals of another population to this one. |
| [sequence](../../cl.ravenhill.keen.utils/sequence.md) | [common]<br>fun &lt;[L](../../cl.ravenhill.keen.utils/sequence.md), [R](../../cl.ravenhill.keen.utils/sequence.md)&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Either&lt;[L](../../cl.ravenhill.keen.utils/sequence.md), [R](../../cl.ravenhill.keen.utils/sequence.md)&gt;&gt;.[sequence](../../cl.ravenhill.keen.utils/sequence.md)(): Either&lt;[L](../../cl.ravenhill.keen.utils/sequence.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.utils/sequence.md)&gt;&gt;<br>Sequences a list of `Either` values, transforming a `List<Either<L, R>>` into an `Either<L, List<R>>`. |
| [sub](../../cl.ravenhill.keen.utils/sub.md) | [common]<br>infix fun [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;.[sub](../../cl.ravenhill.keen.utils/sub.md)(d: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;<br>Subtracts a given value from each element in an iterable collection of doubles. |
| [subList](index.md#423386006%2FFunctions%2F1902964177) | [common]<br>open override fun [subList](index.md#423386006%2FFunctions%2F1902964177)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; |
| [toList](../-population-like/to-list.md) | [common]<br>open fun [toList](../-population-like/to-list.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>Converts the population into a list of individuals. |
| [toPopulation](../to-population.md) | [common]<br>fun &lt;[T](../to-population.md), [F](../to-population.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](../to-population.md), [F](../to-population.md)&gt;, [R](../to-population.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](../to-population.md), [F](../to-population.md)&gt;&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../-individual/index.md)&lt;[T](../to-population.md), [F](../to-population.md), [R](../to-population.md)&gt;&gt;.[toPopulation](../to-population.md)(): [Population](../-population/index.md)&lt;[T](../to-population.md), [F](../to-population.md), [R](../to-population.md)&gt;<br>Converts a list of individuals into a population. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
