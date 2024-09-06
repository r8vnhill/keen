//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.evolution.engines.ga](../index.md)/[GeneticAlgorithmFactory](index.md)/[make](make.md)

# make

[common]\
fun [make](make.md)(): [GeneticAlgorithm](../-genetic-algorithm/index.md)&lt;[T](index.md), [G](index.md), out [EvolutionListener](../../../../keen-core/keen-core/cl.ravenhill.keen.listeners/-evolution-listener/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;, [GeneticEvolutionState](../../cl.ravenhill.keen.evolution.states/-genetic-evolution-state/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;

Constructs and returns a fully configured genetic algorithm instance.

The `make()` function is responsible for assembling the various components and configurations into a complete [GeneticAlgorithm](../-genetic-algorithm/index.md) instance. This method gathers the population configuration, selection configuration, alteration configuration, and evolution configuration into a cohesive structure that defines how the genetic algorithm will operate.

### Example Usage:

```kotlin
val geneticAlgorithm = factory.make()
geneticAlgorithm.evolve()
```

#### Return

A `GeneticAlgorithm` instance configured with the provided settings and ready to be executed.
