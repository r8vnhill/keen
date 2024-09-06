//[keen-core](../../index.md)/[cl.ravenhill.keen](index.md)/[populationOf](population-of.md)

# populationOf

[common]\
fun &lt;[T](population-of.md), [F](population-of.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](population-of.md), [F](population-of.md)&gt;, [R](population-of.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](population-of.md), [F](population-of.md)&gt;&gt; [populationOf](population-of.md)(vararg individuals: [Individual](-individual/index.md)&lt;[T](population-of.md), [F](population-of.md), [R](population-of.md)&gt;): [Population](-population/index.md)&lt;[T](population-of.md), [F](population-of.md), [R](population-of.md)&gt;

Creates a `Population` from a variable number of individuals.

#### Return

A `Population` containing the provided individuals.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
| individuals | The individuals that will form the population. |
