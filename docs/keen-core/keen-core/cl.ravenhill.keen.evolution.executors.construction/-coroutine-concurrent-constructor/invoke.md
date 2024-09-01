//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../index.md)/[CoroutineConcurrentConstructor](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;

Concurrently generates a sequence of values using coroutines.

This `invoke` function is the primary method for generating sequences of values concurrently. It uses the provided `CoroutineScope` to launch asynchronous tasks for each element in the sequence. The function enforces that the size of the sequence must be positive, throwing an [InvalidSizeException](../../cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) if this constraint is violated.

## Constraints:

- 
   **Size Must Be Positive**: The size of the sequence must be greater than 0. If the size is negative, an [InvalidSizeException](../../cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) will be thrown.

#### Return

A list of values generated concurrently.

#### Parameters

common

| | |
|---|---|
| size | The number of elements to generate in the sequence. |
| init | A function that takes an index and returns a value of type `T`, used to initialize each element in     the sequence. |

#### Throws

| | |
|---|---|
| CompositeException | If any of the constraints are violated. |
| [InvalidSizeException](../../cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) | If the size of the sequence is negative; wrapped in a CompositeException. |
