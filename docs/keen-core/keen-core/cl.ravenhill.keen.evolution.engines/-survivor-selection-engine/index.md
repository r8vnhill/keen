//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[SurvivorSelectionEngine](index.md)

# SurvivorSelectionEngine

interface [SurvivorSelectionEngine](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface for implementing survivor selection in an evolutionary algorithm.

The `SurvivorSelectionEngine` interface defines the contract for selecting individuals that will survive into the next generation in an evolutionary algorithm. Survivor selection is a critical component of the evolutionary process, as it determines which individuals from the current population, including offspring generated through genetic operations, will be retained for future generations.

## Theoretical Background:

Survivor selection, also referred to as replacement, plays a pivotal role in evolutionary algorithms. The goal is to maintain a balance between retaining high-quality solutions (individuals with superior fitness) and preserving diversity within the population. This balance is crucial for preventing premature convergence to suboptimal solutions and ensuring the algorithm explores the solution space effectively.

Several strategies can be employed in survivor selection, each with different implications for the evolutionary process:

- 
   **Elitism**: This strategy involves always retaining a certain number of the best individuals, ensuring that the highest-quality solutions are not lost in subsequent generations.
- 
   **Truncation**: In truncation selection, only the top percentage of individuals based on fitness are retained, potentially accelerating convergence but risking the loss of diversity.
- 
   **Tournament Selection**: Random groups (tournaments) of individuals are formed, and the best individuals from each group are selected, providing a balance between selection pressure and diversity.
- 
   **Generational Replacement**: The entire population is replaced by offspring, which can introduce a high degree of diversity but may lead to the loss of potentially valuable solutions.

The choice of survivor selection strategy can significantly affect the convergence speed and effectiveness of the evolutionary algorithm. Aggressive strategies, such as elitism and truncation, may lead to faster convergence but increase the risk of premature convergence (getting stuck in local optima). In contrast, strategies that emphasize diversity, such as tournament selection or generational replacement, may maintain a broader exploration of the solution space but could result in slower overall convergence.

## Usage:

This interface is designed to be implemented by classes that handle the survivor selection phase of evolutionary algorithms. Implementations are expected to encapsulate the logic for selecting which individuals survive into the next generation, and to return the updated evolutionary state after the selection process is completed.

### Example: Implementing a Survivor Selection Engine

```kotlin
class ElitistSurvivorSelection<T, F, R, S> : SurvivorSelectionEngine<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    override suspend fun selectSurvivors(state: S): Either<SelectionException, S> {
        // Implement the elitist survivor selection logic here
        return updatedState.right()
    }
}
```

#### Return

The updated evolutionary state after the survivor selection process.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features within the individuals. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

#### Throws

| | |
|---|---|
| [SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md) | if the survivor selection process fails. |

## Functions

| Name | Summary |
|---|---|
| [selectSurvivors](select-survivors.md) | [common]<br>abstract suspend fun [selectSurvivors](select-survivors.md)(state: [S](index.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](index.md)&gt;<br>Selects the individuals that will survive to the next generation. |
