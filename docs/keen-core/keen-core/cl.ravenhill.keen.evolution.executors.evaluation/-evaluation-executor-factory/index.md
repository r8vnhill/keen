//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[EvaluationExecutorFactory](index.md)

# EvaluationExecutorFactory

class [EvaluationExecutorFactory](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;

Factory class for creating instances of [EvaluationExecutor](../-evaluation-executor/index.md) in evolutionary algorithms.

The `EvaluationExecutorFactory` class is responsible for providing a way to create instances of `EvaluationExecutor` that can evaluate individuals within a population in an evolutionary algorithm. This factory allows for flexibility in choosing the evaluation strategy by configuring the `creator` function.

## Usage:

The factory uses a [creator](creator.md) function to generate `EvaluationExecutor` instances based on the provided evaluation function. By default, it creates a [CoroutineConcurrentEvaluator](../-coroutine-concurrent-evaluator/index.md), which evaluates individuals concurrently using Kotlin coroutines. This setup is ideal for environments where concurrent processing can improve performance.

### Example: Creating an Evaluation Executor

```kotlin
val factory = EvaluationExecutorFactory<MyType, MyFeature, MyRepresentation, MyState>()
val evaluationExecutor = factory.creator { representation ->
    // Custom evaluation logic for the representation
    evaluateFitness(representation)
}
```

In this example, the factory is used to create an `EvaluationExecutor` with a custom evaluation function. The resulting executor can then be used within the evolutionary algorithm to evaluate the fitness of individuals.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Constructors

| | |
|---|---|
| [EvaluationExecutorFactory](-evaluation-executor-factory.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [creator](creator.md) | [common]<br>val [creator](creator.md): (([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) -&gt; [EvaluationExecutor](../-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>A function that creates an [EvaluationExecutor](../-evaluation-executor/index.md) based on the provided evaluation function. |
