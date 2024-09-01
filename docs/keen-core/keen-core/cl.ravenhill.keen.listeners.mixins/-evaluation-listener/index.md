//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[EvaluationListener](index.md)

# EvaluationListener

interface [EvaluationListener](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Interface for listening to the evaluation process in an evolutionary algorithm.

The `EvaluationListener` interface defines methods for monitoring the evaluation stage of an evolutionary algorithm. It allows external observers or components to be notified when the evaluation process starts and ends. This is useful for logging, analysis, or modifying behavior based on the results of evaluation cycles.

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
| [EvolutionSummary](../../cl.ravenhill.keen.listeners.summary/-evolution-summary/index.md) |

## Functions

| Name | Summary |
|---|---|
| [onEvaluationEnd](on-evaluation-end.md) | [common]<br>abstract fun [onEvaluationEnd](on-evaluation-end.md)(state: [S](index.md))<br>Called when the evaluation process ends. |
| [onEvaluationStart](on-evaluation-start.md) | [common]<br>abstract fun [onEvaluationStart](on-evaluation-start.md)(state: [S](index.md))<br>Called when the evaluation process starts. |
