//[keen-core](../../index.md)/[cl.ravenhill.keen](index.md)/[emptyPopulation](empty-population.md)

# emptyPopulation

[common]\
fun &lt;[T](empty-population.md), [F](empty-population.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](empty-population.md), [F](empty-population.md)&gt;, [R](empty-population.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](empty-population.md), [F](empty-population.md)&gt;&gt; [emptyPopulation](empty-population.md)(): [Population](-population/index.md)&lt;[T](empty-population.md), [F](empty-population.md), [R](empty-population.md)&gt;

Creates an empty [Population](-population/index.md).

#### Return

An empty `Population`.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
