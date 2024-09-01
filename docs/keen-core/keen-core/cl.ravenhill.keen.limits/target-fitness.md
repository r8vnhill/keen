//[keen-core](../../index.md)/[cl.ravenhill.keen.limits](index.md)/[targetFitness](target-fitness.md)

# targetFitness

[common]\
fun &lt;[T](target-fitness.md), [F](target-fitness.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](target-fitness.md), [F](target-fitness.md)&gt;, [R](target-fitness.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](target-fitness.md), [F](target-fitness.md)&gt;, [S](target-fitness.md) : [EvolutionState](../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](target-fitness.md), [F](target-fitness.md), [R](target-fitness.md), [S](target-fitness.md)&gt;&gt; [targetFitness](target-fitness.md)(targetFitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): ([ListenerConfiguration](../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](target-fitness.md), [F](target-fitness.md), [R](target-fitness.md)&gt;) -&gt; [TargetFitness](-target-fitness/index.md)&lt;[T](target-fitness.md), [F](target-fitness.md), [R](target-fitness.md), [S](target-fitness.md)&gt;

Factory function to create a [TargetFitness](-target-fitness/index.md) limit condition for an evolutionary algorithm.

The `targetFitness` function provides a convenient way to create instances of the `TargetFitness` class, which serves as a termination condition based on achieving a specified fitness level within the population. This function simplifies the process of defining a fitness-based limit in evolutionary algorithms, improving code readability and reducing the potential for errors.

## Usage:

This factory function is curried, meaning it returns a partially-applied function that can be used to build the `TargetFitness` limit incrementally. This design allows for greater flexibility, particularly when configuring listeners or other parameters separately from the limit condition itself.

#### Return

A function that returns a [TargetFitness](-target-fitness/index.md) instance when invoked, allowing for partial application and flexible configuration.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| targetFitness | The fitness threshold that must be reached by any individual in the population to terminate the evolutionary process. |
