//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[TargetFitness](index.md)/[TargetFitness](-target-fitness.md)

# TargetFitness

[common]\
constructor(targetFitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| targetFitness | The fitness value that, when reached or exceeded by any individual in the population, will cause the evolutionary process to stop. |
| configuration | The configuration settings for the listener associated with this limit condition. |
