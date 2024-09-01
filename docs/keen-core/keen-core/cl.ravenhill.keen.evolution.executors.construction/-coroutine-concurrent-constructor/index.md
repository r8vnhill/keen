//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../index.md)/[CoroutineConcurrentConstructor](index.md)

# CoroutineConcurrentConstructor

class [CoroutineConcurrentConstructor](index.md)&lt;[T](index.md)&gt;(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) : [ConstructorExecutor](../-constructor-executor/index.md)&lt;[T](index.md)&gt; 

A concurrent constructor for generating sequences of values using Kotlin coroutines in an evolutionary algorithm.

The `CoroutineConcurrentConstructor` class is an implementation of the [ConstructorExecutor](../-constructor-executor/index.md) interface that leverages Kotlin coroutines to generate sequences of values concurrently. This approach is well-suited for parallel processing in large-scale or computationally intensive tasks, making it ideal for evolutionary algorithms where sequence generation can be performed asynchronously.

## Usage:

This class is designed to be used in scenarios where concurrent generation of sequences is required. It provides an efficient way to generate elements in parallel by utilizing a CoroutineScope and asynchronous tasks.

### Example: Using `CoroutineConcurrentConstructor` to Create a Sequence

```kotlin
val constructor = CoroutineConcurrentConstructor<Int>()
val sequence = runBlocking {
    constructor(5) { index -> index * 2 }
}
println(sequence) // Output: [0, 2, 4, 6, 8]
```

#### Parameters

common

| | |
|---|---|
| scope | The `CoroutineScope` in which the concurrent operations are executed. Defaults to `CoroutineScope(Dispatchers.Default)`. |

## Constructors

| | |
|---|---|
| [CoroutineConcurrentConstructor](-coroutine-concurrent-constructor.md) | [common]<br>constructor(scope: CoroutineScope = CoroutineScope(Dispatchers.Default))<br>Initializes a new instance of `CoroutineConcurrentConstructor` with the specified `CoroutineScope`. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Concurrently generates a sequence of values using coroutines. |
