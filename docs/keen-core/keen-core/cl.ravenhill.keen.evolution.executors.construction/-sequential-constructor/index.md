//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../index.md)/[SequentialConstructor](index.md)

# SequentialConstructor

class [SequentialConstructor](index.md)&lt;[T](index.md)&gt; : [ConstructorExecutor](../-constructor-executor/index.md)&lt;[T](index.md)&gt; 

A sequential constructor for creating a list of elements in a specified order.

The `SequentialConstructor` class implements the [ConstructorExecutor](../-constructor-executor/index.md) interface, providing a straightforward approach to constructing a list of elements in a sequential manner. This class ensures that elements are generated and initialized in the exact order of their indices, making it ideal for scenarios where the order of elements is crucial, such as constructing chromosomes, genotypes, or other ordered data structures in evolutionary algorithms.

## Usage:

The `SequentialConstructor` is best used when you need to create a list of elements where the order of construction is significant. This constructor generates elements one after the other, ensuring that the list is built in a defined sequence. It is particularly useful when the number of elements is manageable and the order is essential. If performance is a concern, and you need to generate a large number of elements concurrently, consider using the [CoroutineConcurrentConstructor](../-coroutine-concurrent-constructor/index.md) instead.

### Example:

```kotlin
suspend fun example() {
    val list = SequentialConstructor(10) { index -> index * 2 }
    println(list) // Output: [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]
}
```

In this example, the `SequentialConstructor` is used to create a list of integers where each element is twice its index. The list is constructed sequentially, ensuring that the elements are added in the correct order.

#### Parameters

common

| | |
|---|---|
| T | The type of elements to be constructed. |

#### Throws

| | |
|---|---|
| [InvalidSizeException](../../cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) | if the size of the list is negative. |

## Constructors

| | |
|---|---|
| [SequentialConstructor](-sequential-constructor.md) | [common]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md)<br>Companion object to provide a convenient way to create a `SequentialConstructor` instance. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Constructs a list of elements sequentially, initializing each element using the provided function. |
