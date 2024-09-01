//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators](../index.md)/[Operator](index.md)

# Operator

interface [Operator](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;

Represents a generic operator in an evolutionary algorithm that supports both synchronous and asynchronous execution.

The `Operator` interface defines a contract for operators that can be applied to an evolutionary state within an evolutionary algorithm. These operators are responsible for transforming the state, typically by selecting, mutating, or recombining individuals in the population to produce a new state. The interface supports both synchronous and asynchronous operations, thanks to the use of Kotlin's `suspend` functions.

## Usage:

Implement this interface to create custom operators that manipulate the evolutionary state. The `invoke` function, marked as `suspend`, allows the operation to be performed asynchronously if needed, but can also be used in a synchronous context without requiring asynchronous behavior. This flexibility makes the interface suitable for a variety of use cases, from simple, synchronous operations to complex, concurrent tasks.

### Example: Implementing a Custom Operator

```kotlin
class MyOperator : Operator<Int, MyFeature, MyRepresentation> {
    override suspend fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<Int, MyFeature, MyRepresentation>>) -> S,
        random: Random
    ): Result<S> where S : EvolutionState<Int, MyFeature, MyRepresentation> {
        // Custom logic to transform the state, can be synchronous or asynchronous
        // Ensure the output size is valid and matches the resulting population size
    }
}
```

## Implementation Requirements:

When implementing this interface, it's crucial to ensure the following:

- 
   **Validate Output Size**: Implementers must validate that the `outputSize` parameter is appropriate for the operation. The output size must not exceed the population size or be negative.
- 
   **Match Output Size**: The resulting population size after the operation should exactly match the specified `outputSize`. This is essential to maintain consistency within the evolutionary algorithm.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

#### Inheritors

| |
|---|
| [Alterer](../../cl.ravenhill.keen.operators.alteration/-alterer/index.md) |
| [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>abstract suspend operator fun &lt;[S](invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[OperatorInvocationException](../../cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md), [S](invoke.md)&gt;<br>Applies the operator to the given evolutionary state to produce a new state, supporting both synchronous and asynchronous execution. |
