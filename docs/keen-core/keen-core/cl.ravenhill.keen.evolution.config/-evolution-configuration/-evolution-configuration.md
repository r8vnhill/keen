//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.config](../index.md)/[EvolutionConfiguration](index.md)/[EvolutionConfiguration](-evolution-configuration.md)

# EvolutionConfiguration

[common]\
constructor(limits: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Limit](../../cl.ravenhill.keen.limits/-limit/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md), [Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;&gt;, listeners: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Listener](../../cl.ravenhill.keen.listeners/-listener/index.md)&gt;, ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, evaluator: [EvaluationExecutor](../../cl.ravenhill.keen.evolution.executors.evaluation/-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, interceptor: [EvolutionInterceptor](../../cl.ravenhill.keen.evolution/-evolution-interceptor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;, initialState: [S](index.md))

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features in the evolutionary algorithm. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
