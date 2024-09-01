//[keen-core](../../index.md)/[cl.ravenhill.keen.limits](index.md)/[maxGenerations](max-generations.md)

# maxGenerations

[common]\
fun &lt;[T](max-generations.md), [F](max-generations.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](max-generations.md), [F](max-generations.md)&gt;, [R](max-generations.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](max-generations.md), [F](max-generations.md)&gt;, [S](max-generations.md) : [EvolutionState](../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](max-generations.md), [F](max-generations.md), [R](max-generations.md), [S](max-generations.md)&gt;&gt; [maxGenerations](max-generations.md)(maxGenerations: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): ([ListenerConfiguration](../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](max-generations.md), [F](max-generations.md), [R](max-generations.md)&gt;) -&gt; [MaxGenerations](-max-generations/index.md)&lt;[T](max-generations.md), [F](max-generations.md), [R](max-generations.md), [S](max-generations.md)&gt;

Factory function for creating a `MaxGenerations` limit condition.

The `maxGenerations` function is a convenient way to create instances of the [MaxGenerations](-max-generations/index.md) class. It simplifies the process of defining a generation-based limit in evolutionary algorithms, making the code more readable and reducing the chance of errors.

### Example: Using the `maxGenerations` Factory Function

```kotlin
val limitFactory = maxGenerations<MyType, MyFeature, MyRepresentation, MyState>(maxGenerations = 100)
val limit = limitFactory(myListenerConfiguration)
```

#### Return

A function that returns a `MaxGenerations` instance when invoked with a listener configuration.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| maxGenerations | The maximum number of generations allowed before stopping the evolutionary process. |
