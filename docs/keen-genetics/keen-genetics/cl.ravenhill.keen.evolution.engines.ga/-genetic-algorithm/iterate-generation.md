//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithm](index.md)/[iterateGeneration](iterate-generation.md)

# iterateGeneration

[common]\
open suspend override fun [iterateGeneration](iterate-generation.md)(state: [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;): Either&lt;[EvolutionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evolution-exception/index.md), [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Advances the evolutionary process by one generation.

The `iterateGeneration` function guides the algorithm through a complete cycle of evolutionary steps for one generation. This method actively manages the process, from initialization to generation advancement, ensuring each phase is executed in the correct sequence. If any phase fails, the function stops and returns an [EvolutionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evolution-exception/index.md).

## Workflow:

1. 
   **Pre-Initialization Interception**: The function intercepts and potentially modifies the state before initialization begins.
2. 
   **Initialization**: If the population is empty, the function initializes it, creating a well-defined starting point. Any errors during this step cause the generation to halt.
3. 
   **Evaluation**: The function evaluates the population based on the fitness criteria. If the evaluation fails, the function stops the process.
4. 
   **Parent Selection**: The function selects parents from the evaluated population for reproduction. If the selection fails, the function halts the process.
5. 
   **Survivor Selection**: The function chooses which individuals will survive to the next generation. Any failure during this phase stops the generation.
6. 
   **Alteration**: The selected parents undergo genetic operations (e.g., crossover, mutation) to produce offspring. If this process fails, the function stops the generation.
7. 
   **Population Update**: The function forms the next generation by combining the survivors with the newly created offspring and then re-evaluates the population.
8. 
   **Post-Evaluation Interception**: After the evaluation, the function intercepts the state again, allowing for any final adjustments before proceeding to the next generation.
9. 
   **Generation Advancement**: The function increments the generation counter and returns the updated state.

## Error Handling:

The function carefully monitors each step for errors. If an error occurs, it returns an [EvolutionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evolution-exception/index.md) within an Either.Left value. On success, it returns the updated [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md) within an Either.Right value.

#### Return

An Either containing the updated [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md) after one generation on success, or an [EvolutionException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-evolution-exception/index.md) on failure.

#### Parameters

common

| | |
|---|---|
| state | The current [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md) representing the evolutionary state at the start of the generation. |
