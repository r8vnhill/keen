//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[InitializerEngine](index.md)/[initialize](initialize.md)

# initialize

[common]\
abstract suspend fun [initialize](initialize.md)(state: [S](index.md)): Either&lt;[InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [S](index.md)&gt;

Initializes the evolutionary state.

This method is responsible for setting up the initial conditions of the evolutionary algorithm, such as generating the initial population. The process is expected to be asynchronous, making use of coroutines for non-blocking execution.

The method returns an `Either` type to handle potential failures:

- 
   On success, the initialized state is returned.
- 
   On failure, an [InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md) is returned, providing details about the error.

#### Return

An Either containing the initialized state or an [InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md).

#### Parameters

common

| | |
|---|---|
| state | The current state that needs initialization. |
