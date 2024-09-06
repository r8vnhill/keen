//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMaxRanker](../index.md)/[Companion](index.md)/[async](async.md)

# async

[common]\
fun &lt;[T](async.md), [F](async.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](async.md), [F](async.md)&gt;, [R](async.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](async.md), [F](async.md)&gt;&gt; [async](async.md)(chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE): [FitnessMaxRanker.AsyncFitnessMaxRanker](../-async-fitness-max-ranker/index.md)&lt;[T](async.md), [F](async.md), [R](async.md)&gt;

Creates an asynchronous instance of `FitnessMaxRanker`.

#### Return

An [AsyncFitnessMaxRanker](../-async-fitness-max-ranker/index.md) that ranks individuals by maximizing their fitness.
