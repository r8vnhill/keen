//[keen-core](../../index.md)/[cl.ravenhill.keen](index.md)/[fitness](fitness.md)

# fitness

[common]\
val &lt;[T](fitness.md), [F](fitness.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](fitness.md), [F](fitness.md)&gt;, [R](fitness.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](fitness.md), [F](fitness.md)&gt;&gt; [Population](-population/index.md)&lt;[T](fitness.md), [F](fitness.md), [R](fitness.md)&gt;.[fitness](fitness.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;

Extension property to get the fitness values of the individuals in the population.

This extension property provides a convenient way to retrieve the fitness values of all individuals in a population. The fitness values are computed by mapping over the population and extracting the fitness of each individual.

## Usage:

Use this property to quickly access the fitness values of all individuals in a population, which is useful for analysis, selection, and ranking processes in evolutionary algorithms.

### Example:

```kotlin
val population: Population<MyType, MyFeature, MyRepresentation> = // initialize population
val fitnessValues = population.fitness
println(fitnessValues) // Output: List of fitness values
```
