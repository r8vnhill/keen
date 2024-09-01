//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[EvaluationExecutorFactory](index.md)/[creator](creator.md)

# creator

[common]\
val [creator](creator.md): (([R](index.md)) -&gt; [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) -&gt; [EvaluationExecutor](../-evaluation-executor/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;

A function that creates an [EvaluationExecutor](../-evaluation-executor/index.md) based on the provided evaluation function.

By default, this function creates a `CoroutineConcurrentEvaluator` which evaluates individuals concurrently.
