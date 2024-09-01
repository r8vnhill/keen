//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.crossover](../index.md)/[Crossover](index.md)/[crossover](crossover.md)

# crossover

[common]\
open fun [crossover](crossover.md)(parentGenotypes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): Either&lt;[CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Genotype](../../cl.ravenhill.keen.genetics/-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;&gt;

Performs a crossover operation on a list of parent genotypes to produce offspring genotypes in a genetic algorithm.

The `crossover` function is a core operation in genetic algorithms, responsible for combining genetic material from parent genotypes to create new offspring. This operation introduces genetic diversity into the population, which is essential for exploring the solution space and avoiding premature convergence to local optima.

## Recommended Usage:

Although the `crossover` function is available for direct use, it is recommended to perform crossover operations using the [invoke](invoke.md) operator of the [Crossover](index.md) interface. The `invoke` operator provides a higher-level abstraction and handles additional logic that ensures the crossover operation is integrated seamlessly into the evolutionary algorithm's lifecycle.

#### Return

An Either containing a list of offspring genotypes on the right, or a [CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md) on the left in case of an error.

#### Parameters

common

| | |
|---|---|
| parentGenotypes | A list of parent genotypes to be used in the crossover operation. |

#### Throws

| | |
|---|---|
| [CrossoverException](../../cl.ravenhill.keen.exceptions/-crossover-exception/index.md) | if the input parent genotypes do not meet the required constraints, or if the crossover operation fails for any reason. |
