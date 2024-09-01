//[keen-core](../../index.md)/[cl.ravenhill.keen.limits](index.md)/[limit](limit.md)

# limit

[common]\
fun &lt;[T](limit.md), [F](limit.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](limit.md), [F](limit.md)&gt;, [R](limit.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](limit.md), [F](limit.md)&gt;, [S](limit.md) : [EvolutionState](../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](limit.md), [F](limit.md), [R](limit.md), [S](limit.md)&gt;, [L](limit.md) : [Listener](../cl.ravenhill.keen.listeners/-listener/index.md)&gt; [limit](limit.md)(builder: ([ListenerConfiguration](../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](limit.md), [F](limit.md), [R](limit.md)&gt;) -&gt; [L](limit.md), predicate: [L](limit.md).([S](limit.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): ([ListenerConfiguration](../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](limit.md), [F](limit.md), [R](limit.md)&gt;) -&gt; [Limit](-limit/index.md)&lt;[T](limit.md), [F](limit.md), [R](limit.md), [S](limit.md), [L](limit.md)&gt;

Factory function for creating a [Limit](-limit/index.md) instance with a custom predicate.

The `limit` function simplifies the creation of `Limit` instances by accepting a builder function for the listener and a predicate function that defines the limit condition. This function is useful for creating custom stopping criteria in evolutionary algorithms, with the flexibility to specify the listener and the predicate at runtime.

### Example: Creating a Limit with a Custom Predicate

```kotlin
val myLimit = limit<MyType, MyFeature, MyRepresentation, MyState, MyListener>(
    builder = { config -> MyListener(config) },
    predicate = { state -> state.population.any { it.fitness >= 0.95 } }
)
```

In this example, `myLimit` will stop the evolutionary process once any individual's fitness in the population reaches or exceeds 0.95.

#### Return

A function that, when provided with a listener configuration, returns a `Limit` instance.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| L | The type of the listener, which must extend [Listener](../cl.ravenhill.keen.listeners/-listener/index.md). |
| builder | A function that constructs the listener based on the provided configuration. |
| predicate | A predicate function that evaluates the state and determines whether the limit has been reached. |
