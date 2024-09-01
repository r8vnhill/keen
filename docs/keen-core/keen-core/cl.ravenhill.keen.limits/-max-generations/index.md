//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[MaxGenerations](index.md)

# MaxGenerations

class [MaxGenerations](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(val maxGenerations: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) : [Limit](../-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [MaxGenerationsListener](../-max-generations-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt; 

A limit condition for evolutionary algorithms based on the maximum number of generations.

The `MaxGenerations` class defines a stopping condition for an evolutionary algorithm that halts the process once the number of generations reaches or exceeds a specified maximum. This limit is particularly useful in scenarios where the evolutionary process needs to be bounded by a fixed number of iterations, regardless of the fitness levels achieved.

### Recommendation:

It is recommended to use the `maxGenerations` curried function instead of directly instantiating the `MaxGenerations` class. The curried function provides a more concise and flexible way to configure the limit, allowing for partial application and easier reuse in different contexts.

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

## Constructors

| | |
|---|---|
| [MaxGenerations](-max-generations.md) | [common]<br>constructor(maxGenerations: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), configuration: [ListenerConfiguration](../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [listener](../-limit/listener.md) | [common]<br>val [listener](../-limit/listener.md): [MaxGenerationsListener](../-max-generations-listener/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>The listener that observes the evolutionary process and applies the limit condition. |
| [maxGenerations](max-generations.md) | [common]<br>val [maxGenerations](max-generations.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-limit/invoke.md) | [common]<br>operator fun [invoke](../-limit/invoke.md)(state: [S](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Evaluates the limit condition on the given state. |
