//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[EvaluationEngine](index.md)

# EvaluationEngine

interface [EvaluationEngine](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface representing an evaluation engine in an evolutionary algorithm.

The `EvaluationEngine` interface defines the contract for engines responsible for evaluating the population within an evolutionary state. Evaluation is a crucial step in evolutionary algorithms, as it determines the fitness of individuals in the population based on their representations. This interface is designed to be flexible and supports asynchronous operations by utilizing Kotlin's `suspend` functions, making it suitable for environments where non-blocking operations are essential.

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
| [evaluate](evaluate.md) | [common]<br>abstract suspend fun [evaluate](evaluate.md)(state: [S](index.md)): Either&lt;[EvaluationException](../../cl.ravenhill.keen.exceptions/-evaluation-exception/index.md), [S](index.md)&gt;<br>Evaluates the population within the given evolutionary state. |
