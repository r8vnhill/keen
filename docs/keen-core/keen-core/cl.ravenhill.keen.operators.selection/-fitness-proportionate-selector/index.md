//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[FitnessProportionateSelector](index.md)

# FitnessProportionateSelector

typealias [FitnessProportionateSelector](index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = [RouletteWheelSelector](../-roulette-wheel-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;

Typealias for a [RouletteWheelSelector](../-roulette-wheel-selector/index.md), representing the fitness-proportionate selection strategy.

The `FitnessProportionateSelector` is a typealias for the `RouletteWheelSelector`, which implements the fitness-proportionate selection strategy, commonly known as roulette wheel selection, in evolutionary algorithms. This strategy selects individuals based on their relative fitness within the population, with higher fitness individuals having a greater probability of being selected.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
