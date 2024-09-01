//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.config](../index.md)/[EvolutionConfiguration](index.md)

# EvolutionConfiguration

data class [EvolutionConfiguration](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;, [S](index.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;&gt;(val limits: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Limit](../../cl.ravenhill.keen.limits/-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;&gt;, val listeners: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;, val ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, val evaluator: [EvaluationExecutor](../../cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, val interceptor: [EvolutionInterceptor](../../cl.ravenhill.keen.evolution/-evolution-interceptor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, val initialState: [S](index.md))

Configuration class for setting up an evolutionary algorithm.

The `EvolutionConfiguration` data class encapsulates the configuration details required to run an evolutionary algorithm. It holds the limits and listeners that govern the algorithm's execution, allowing users to define termination conditions and monitor the evolutionary process through various lifecycle events.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features in the evolutionary algorithm. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |

## Constructors

| | |
|---|---|
| [EvolutionConfiguration](-evolution-configuration.md) | [common]<br>constructor(limits: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Limit](../../cl.ravenhill.keen.limits/-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;&gt;, listeners: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;, ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, evaluator: [EvaluationExecutor](../../cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, interceptor: [EvolutionInterceptor](../../cl.ravenhill.keen.evolution/-evolution-interceptor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, initialState: [S](index.md)) |

## Properties

| Name | Summary |
|---|---|
| [evaluator](evaluator.md) | [common]<br>val [evaluator](evaluator.md): [EvaluationExecutor](../../cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>An [EvaluationExecutor](../../cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor/index.md) that computes the fitness of each individual in the population. |
| [initialState](initial-state.md) | [common]<br>val [initialState](initial-state.md): [S](index.md)<br>The initial state of the evolutionary process. |
| [interceptor](interceptor.md) | [common]<br>val [interceptor](interceptor.md): [EvolutionInterceptor](../../cl.ravenhill.keen.evolution/-evolution-interceptor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;<br>An [EvolutionInterceptor](../../cl.ravenhill.keen.evolution/-evolution-interceptor/index.md) that can modify the behavior of the evolutionary process. |
| [limits](limits.md) | [common]<br>val [limits](limits.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Limit](../../cl.ravenhill.keen.limits/-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;&gt;<br>A list of [Limit](../../cl.ravenhill.keen.limits/-limit/index.md)s that define the stopping conditions for the evolutionary algorithm. |
| [listeners](listeners.md) | [common]<br>val [listeners](listeners.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;<br>A list of [EvolutionListener](../../cl.ravenhill.keen.listeners/-evolution-listener/index.md)s that monitor the lifecycle of the evolutionary process. |
| [ranker](ranker.md) | [common]<br>val [ranker](ranker.md): [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>An [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md) that assigns a fitness value to each individual in the population. |
