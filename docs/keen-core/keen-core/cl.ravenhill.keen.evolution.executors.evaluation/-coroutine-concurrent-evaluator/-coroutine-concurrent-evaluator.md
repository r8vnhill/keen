//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[CoroutineConcurrentEvaluator](index.md)/[CoroutineConcurrentEvaluator](-coroutine-concurrent-evaluator.md)

# CoroutineConcurrentEvaluator

[common]\
constructor(evaluationFunction: ([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), coroutineContext: [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) = Dispatchers.Default)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features within the individuals. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of evolutionary state, which must extend [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
