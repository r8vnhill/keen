//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators](../index.md)/[Operator](index.md)/[invoke](invoke.md)

# invoke

[common]\
abstract suspend operator fun &lt;[S](invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[OperatorInvocationException](../../cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md), [S](invoke.md)&gt;

Applies the operator to the given evolutionary state to produce a new state, supporting both synchronous and asynchronous execution.

This `suspend` function is the core operation of the `Operator` interface, transforming the current evolutionary state by selecting, mutating, or recombining individuals in the population. The transformation is based on the provided parameters and a random number generator. The function is marked as `suspend` to support asynchronous operations, but it can also be used synchronously if asynchronous behavior is not required.

## Implementation Note:

Implementers are responsible for validating that the [outputSize](invoke.md) is appropriate and ensuring that the new population's size matches the `outputSize`. This is crucial for maintaining the integrity of the evolutionary process and preventing runtime errors.

#### Return

An Either containing the new evolutionary state, or a [OperatorInvocationException](../../cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md) if the operation fails.

#### Parameters

common

| | |
|---|---|
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
| state | The current evolutionary state. |
| outputSize | The number of individuals to include in the resulting state. |
| buildState | A function that constructs the new evolutionary state from a list of individuals. |
