//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](../index.md)/[ConstructorExecutor](index.md)

# ConstructorExecutor

interface [ConstructorExecutor](index.md)&lt;[T](index.md)&gt;

Interface for constructing a list of elements either concurrently or sequentially.

The `ConstructorExecutor` interface provides a blueprint for creating lists of elements of type [T](index.md). Implementations of this interface can determine whether the construction process occurs sequentially or concurrently. This is particularly valuable in scenarios like evolutionary algorithms, where elements such as chromosomes or genotypes are generated or initialized based on their index in the list.

## Usage:

To use the `ConstructorExecutor`, implement the interface and define the [invoke](invoke.md) method to control how elements are constructed. The method generates a list of elements, where each element is initialized by a provided function.

### Example Implementations:

- 
   **Sequential Execution**:

```kotlin
class SequentialConstructorExecutor<T> : ConstructorExecutor<T> {
    override suspend fun invoke(size: Int, init: suspend (index: Int) -> T) =
        List(size) { index -> init(index) }
}
```

- 
   **Concurrent Execution**:

```kotlin
class ConcurrentConstructorExecutor<T> : ConstructorExecutor<T> {
    override suspend fun invoke(size: Int, init: suspend (index: Int) -> T): List<T> =
        coroutineScope {
            (0 until size).map { index ->
                async { init(index) }
            }.awaitAll()
        }
}
```

In the examples above, `SequentialConstructorExecutor` constructs the list elements one after the other, while `ConcurrentConstructorExecutor` uses coroutines to construct elements in parallel, potentially improving performance in scenarios with expensive initialization logic.

#### Parameters

common

| | |
|---|---|
| T | The type of elements that the list will contain. |

#### Inheritors

| |
|---|
| [CoroutineConcurrentConstructor](../-coroutine-concurrent-constructor/index.md) |
| [SequentialConstructor](../-sequential-constructor/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>abstract suspend operator fun [invoke](invoke.md)(size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), init: suspend (index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [T](index.md)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Constructs a list of elements of the specified size, initializing each element using the provided function. |
