//[keen-core](../../index.md)/[cl.ravenhill.keen.listeners](index.md)/[fittest](fittest.md)

# fittest

[common]\
suspend fun &lt;[T](fittest.md), [F](fittest.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](fittest.md), [F](fittest.md)&gt;, [R](fittest.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](fittest.md), [F](fittest.md)&gt;&gt; [fittest](fittest.md)(ranker: [IndividualRanker](../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](fittest.md), [F](fittest.md), [R](fittest.md)&gt;, record: [EvolutionRecord](../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](fittest.md), [F](fittest.md), [R](fittest.md)&gt;): [IndividualRecord](../cl.ravenhill.keen.listeners.records/-individual-record/index.md)&lt;[T](fittest.md), [F](fittest.md), [R](fittest.md)&gt;

Determines the fittest individual from the most recent generation in the evolutionary record.

The `fittest` function identifies the individual with the highest fitness value from the offspring population of the most recent generation recorded in the provided [EvolutionRecord](../cl.ravenhill.keen.listeners.records/-evolution-record/index.md). This function uses the given [IndividualRanker](../cl.ravenhill.keen.ranking/-individual-ranker/index.md) to sort the individuals based on their fitness and then selects the top individual.

#### Return

The fittest [IndividualRecord](../cl.ravenhill.keen.listeners.records/-individual-record/index.md) from the most recent generation's offspring population.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features within the individual. |
| F | The type of feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
| ranker | The [IndividualRanker](../cl.ravenhill.keen.ranking/-individual-ranker/index.md) used to evaluate and rank the individuals based on their fitness. |
| record | The [EvolutionRecord](../cl.ravenhill.keen.listeners.records/-evolution-record/index.md) containing the evolutionary history, including all generations and their populations. |

#### Throws

| | |
|---|---|
| [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) | if the evolution record is empty or if the most recent generation has no offspring. |
