//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[MaxGenerations](index.md)/[MaxGenerations](-max-generations.md)

# MaxGenerations

[common]\
constructor(maxGenerations: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| maxGenerations | The maximum number of generations allowed before stopping the evolutionary process. |
| configuration | The configuration object that contains listeners and other settings required by the listener. |
