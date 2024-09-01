//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics](../index.md)/[GenotypeFactory](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun [invoke](invoke.md)(): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [Genotype](../-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Generates a new genotype by assembling a collection of chromosomes.

This method is responsible for creating a `Genotype` instance by invoking each `ChromosomeFactory` in the `chromosomes` list. The creation process is managed by the `ConstructorExecutor`, which can handle the generation of chromosomes concurrently or sequentially depending on its implementation.

#### Return

An Either containing a `Genotype` instance if successful, or an [InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md) if an error occurs during the construction process.
