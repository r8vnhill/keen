//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMinRanker](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [async](async.md) | [common]<br>fun &lt;[T](async.md), [F](async.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](async.md), [F](async.md)&gt;, [R](async.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](async.md), [F](async.md)&gt;&gt; [async](async.md)(chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE): [FitnessMinRanker.AsyncFitnessMinRanker](../-async-fitness-min-ranker/index.md)&lt;[T](async.md), [F](async.md), [R](async.md)&gt;<br>Creates an asynchronous instance of `FitnessMinRanker`. |
| [sync](sync.md) | [common]<br>fun &lt;[T](sync.md), [F](sync.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](sync.md), [F](sync.md)&gt;, [R](sync.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](sync.md), [F](sync.md)&gt;&gt; [sync](sync.md)(): [FitnessMinRanker.SyncFitnessMinRanker](../-sync-fitness-min-ranker/index.md)&lt;[T](sync.md), [F](sync.md), [R](sync.md)&gt;<br>Creates a synchronous instance of `FitnessMinRanker`. |
