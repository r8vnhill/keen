//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[InitializerEngine](index.md)

# InitializerEngine

interface [InitializerEngine](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface representing an initialization engine for evolutionary algorithms.

The `InitializerEngine` interface defines the contract for components responsible for initializing the evolutionary state in an evolutionary algorithm. This typically involves generating an initial population or setting up other state-related data structures that the algorithm will use in subsequent generations.

## Responsibilities:

Implementations of this interface are expected to:

- 
   **Initialization**: Provide logic to initialize the evolutionary state, typically by generating a population of individuals or configuring other necessary components.
- 
   **Error Handling**: Use the `Either` type to handle potential failures during initialization, encapsulating errors in an [InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md) if something goes wrong.
- 
   **Asynchronous Execution**: Support asynchronous operations through the `suspend` modifier, allowing the initialization process to be non-blocking and easily integrated into coroutine-based workflows.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Functions

| Name | Summary |
|---|---|
| [initialize](initialize.md) | [common]<br>abstract suspend fun [initialize](initialize.md)(state: [S](index.md)): Either&lt;[InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [S](index.md)&gt;<br>Initializes the evolutionary state. |
