//[keen-core](../../../../index.md)/[cl.ravenhill.keen.ranking](../../index.md)/[FitnessMaxRanker](../index.md)/[AsyncFitnessMaxRanker](index.md)/[AsyncFitnessMaxRanker](-async-fitness-max-ranker.md)

# AsyncFitnessMaxRanker

[common]\
constructor(chunkSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = DEFAULT_CHUNK_SIZE)

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the individuals. |
| F | The type of feature used in the individual's representation. |
| R | The type of representation used by the individual. |
| chunkSize | The size of the chunks into which the population is divided for parallel sorting. The default value is [DEFAULT_CHUNK_SIZE](../../../../../keen-core/cl.ravenhill.keen.ranking/-fitness-max-ranker/-async-fitness-max-ranker/-companion/-d-e-f-a-u-l-t_-c-h-u-n-k_-s-i-z-e.md). |
