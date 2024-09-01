//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[EvolutionState](index.md)/[flatten](flatten.md)

# flatten

[common]\
open override fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;

Flattens the population by combining all the individuals' representations into a single list.

This method traverses through each individual in the population and flattens their representations (e.g., genes within chromosomes) into a single, unified list. The result is a list that contains all the flattened elements from every individual in the population.

#### Return

A list containing all the flattened elements from the population.
