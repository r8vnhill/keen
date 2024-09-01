//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners](../../index.md)/[Listener](../index.md)/[Companion](index.md)/[computeSteadyGenerations](compute-steady-generations.md)

# computeSteadyGenerations

[common]\
fun &lt;[T](compute-steady-generations.md), [F](compute-steady-generations.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](compute-steady-generations.md), [F](compute-steady-generations.md)&gt;, [R](compute-steady-generations.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](compute-steady-generations.md), [F](compute-steady-generations.md)&gt;&gt; [computeSteadyGenerations](compute-steady-generations.md)(ranker: [IndividualRanker](../../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](compute-steady-generations.md), [F](compute-steady-generations.md), [R](compute-steady-generations.md)&gt;, evolution: [EvolutionRecord](../../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](compute-steady-generations.md), [F](compute-steady-generations.md), [R](compute-steady-generations.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Computes the number of steady generations in an evolutionary process.

The `computeSteadyGenerations` function calculates how many consecutive generations in an evolutionary process have produced the fittest individual with the same fitness value. This can be used to determine if the evolutionary process has reached a point of stagnation, where the population is no longer improving in fitness.

The function iterates through the generations in reverse order, comparing the fittest individual of each generation with the fittest individual of the previous generation. If the fitness values are equal, the function increments the `steady` counter. The process continues until a difference in fitness is found or all generations are checked.

#### Return

The number of steady generations, i.e., generations where the fittest individual has the same fitness value.

#### Parameters

common

| | |
|---|---|
| ranker | The [IndividualRanker](../../../cl.ravenhill.keen.ranking/-individual-ranker/index.md) used to evaluate and compare individuals within the population. |
| evolution | The [EvolutionRecord](../../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md) containing the history of generations to be analyzed. |
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md). |
