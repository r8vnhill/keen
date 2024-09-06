//[keen-core](../../index.md)/[cl.ravenhill.keen](index.md)/[toPopulation](to-population.md)

# toPopulation

[common]\
fun &lt;[T](to-population.md), [F](to-population.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](to-population.md), [F](to-population.md)&gt;, [R](to-population.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](to-population.md), [F](to-population.md)&gt;&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](-individual/index.md)&lt;[T](to-population.md), [F](to-population.md), [R](to-population.md)&gt;&gt;.[toPopulation](to-population.md)(): [Population](-population/index.md)&lt;[T](to-population.md), [F](to-population.md), [R](to-population.md)&gt;

Converts a list of individuals into a population.

#### Return

A `Population` containing the individuals in the original list.

#### Receiver

A `List` of `Individual` instances.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the feature. |
| F | The type of the feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
