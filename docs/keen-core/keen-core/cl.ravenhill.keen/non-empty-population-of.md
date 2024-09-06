//[keen-core](../../index.md)/[cl.ravenhill.keen](index.md)/[nonEmptyPopulationOf](non-empty-population-of.md)

# nonEmptyPopulationOf

[common]\
fun &lt;[T](non-empty-population-of.md), [F](non-empty-population-of.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](non-empty-population-of.md), [F](non-empty-population-of.md)&gt;, [R](non-empty-population-of.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](non-empty-population-of.md), [F](non-empty-population-of.md)&gt;&gt; [nonEmptyPopulationOf](non-empty-population-of.md)(individuals: NonEmptyList&lt;[Individual](-individual/index.md)&lt;[T](non-empty-population-of.md), [F](non-empty-population-of.md), [R](non-empty-population-of.md)&gt;&gt;): [NonEmptyPopulation](-non-empty-population/index.md)&lt;[T](non-empty-population-of.md), [F](non-empty-population-of.md), [R](non-empty-population-of.md)&gt;

Creates a `NonEmptyPopulation` from a `NonEmptyList` of individuals.

#### Return

A [NonEmptyPopulation](-non-empty-population/index.md) containing the provided individuals.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
| individuals | A `NonEmptyList` of individuals that will form the population. |
