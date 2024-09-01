//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AbstractEvolver](index.md)/[iterateGeneration](iterate-generation.md)

# iterateGeneration

[common]\
abstract suspend fun [iterateGeneration](iterate-generation.md)(state: [S](index.md)): Either&lt;[EvolutionException](../../cl.ravenhill.keen.exceptions/-evolution-exception/index.md), [S](index.md)&gt;

Advances the evolutionary state by one generation.

Subclasses must implement this method to define the specific logic for updating the evolutionary state in each generation.

The method should return an Either value, where the left side indicates an error condition and the right side indicates a successful operation. If an error occurs, the method should return an Either.Left value containing an [EvolutionException](../../cl.ravenhill.keen.exceptions/-evolution-exception/index.md) describing the error. If the operation is successful, the method should return an Either.Right value containing the updated evolutionary state.

#### Return

The updated evolutionary state after one generation.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state. |
