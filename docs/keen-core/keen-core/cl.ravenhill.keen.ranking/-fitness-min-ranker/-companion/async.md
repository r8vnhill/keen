//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMinRanker](../index.md)/[Companion](index.md)/[async](async.md)

# async

[common]\
fun &lt;[T](async.md), [F](async.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](async.md), [F](async.md)&gt;, [R](async.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](async.md), [F](async.md)&gt;&gt; [async](async.md)(chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE): [FitnessMinRanker.AsyncFitnessMinRanker](../-async-fitness-min-ranker/index.md)&lt;[T](async.md), [F](async.md), [R](async.md)&gt;

Creates an asynchronous instance of `FitnessMinRanker`.

#### Return

An [AsyncFitnessMinRanker](../-async-fitness-min-ranker/index.md) that ranks individuals by minimizing their fitness. Default chunk size is [DEFAULT_CHUNK_SIZE](../../../../../keen-core/cl.ravenhill.keen.ranking/-fitness-min-ranker/-companion/-d-e-f-a-u-l-t_-c-h-u-n-k_-s-i-z-e.md).
