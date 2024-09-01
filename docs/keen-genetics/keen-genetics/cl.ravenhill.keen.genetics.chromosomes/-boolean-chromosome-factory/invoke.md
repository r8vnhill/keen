//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[BooleanChromosomeFactory](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun [invoke](invoke.md)(): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [Chromosome](../-chromosome/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;&gt;

Asynchronously creates a `BooleanChromosome` of the specified size.

This `invoke` function is the primary method for generating a `BooleanChromosome`. It uses the provided `Random` instance and the configured `trueRate` to determine the boolean value of each gene in the chromosome. The function enforces that the size of the chromosome must be defined and positive, throwing an [InvalidSizeException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) if these constraints are violated.

## Constraints:

- 
   **Size Must Be Defined**: The size property must be initialized before invoking this method. If the size is not defined, an [InvalidSizeException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) will be thrown.
- 
   **Size Must Be Positive**: The size of the chromosome must be greater than 0. If the size is less than 1, an [InvalidSizeException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-invalid-size-exception/index.md) will be thrown.

#### Return

A [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html) containing the generated `BooleanChromosome`, or an exception if the generation fails.
