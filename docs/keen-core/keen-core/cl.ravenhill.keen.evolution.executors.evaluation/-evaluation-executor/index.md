//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[EvaluationExecutor](index.md)

# EvaluationExecutor

interface [EvaluationExecutor](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface for executing the evaluation process in an evolutionary algorithm.

The `EvaluationExecutor` interface defines the contract for evaluating a population of individuals within an evolutionary state. This evaluation process typically involves calculating the fitness of each individual based on a provided fitness function or evaluation criteria. The result of the evaluation influences the selection, mutation, and crossover processes in subsequent generations.

## Key Features:

- 
   **Evaluation Process**: The interface supports flexible evaluation strategies, allowing the evaluation of individuals that are newly created, those that have changed, or forcing the re-evaluation of all individuals.
- 
   **Force Evaluation**: The `force` parameter allows control over whether to evaluate only new or changed individuals, or to force the evaluation of the entire population, regardless of whether they have been evaluated previously.

## Usage:

This interface is intended to be implemented by classes that manage the evaluation process in an evolutionary algorithm. Implementations should define how the evaluation is performed and how the results are stored or used within the evolutionary state.

### Example: Implementing a Custom EvaluationExecutor

```kotlin
class MyEvaluationExecutor<T, F, R, S> : EvaluationExecutor<T, F, R, S>
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> {

    override fun invoke(state: S, force: ForceEvaluation = ForceEvaluation.NEW): S {
        // Implement the evaluation logic here
        // For example, evaluate the fitness of each individual in the state
        state.population.forEach { individual ->
            if (force == ForceEvaluation.ALL || !individual.isEvaluated) {
                individual.fitness = calculateFitness(individual.representation)
            }
        }
        return state // Return the updated state with evaluated individuals
    }
    // ... Additional methods and logic ...
}
```

#### Return

The updated evolutionary state after the evaluation process.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

#### Inheritors

| |
|---|
| [CoroutineConcurrentEvaluator](../-coroutine-concurrent-evaluator/index.md) |
| [SequentialEvaluator](../-sequential-evaluator/index.md) |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>abstract suspend operator fun [invoke](invoke.md)(state: [S](index.md), force: [ForceEvaluation](../-force-evaluation/index.md) = ForceEvaluation.NEW): [S](index.md)<br>Executes the evaluation process on the evolutionary state. |
