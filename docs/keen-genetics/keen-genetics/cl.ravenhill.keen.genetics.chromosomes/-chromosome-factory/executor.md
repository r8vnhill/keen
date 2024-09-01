//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[ChromosomeFactory](index.md)/[executor](executor.md)

# executor

[common]\
abstract var [executor](executor.md): [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[G](index.md)&gt;

The `ConstructorExecutor` used to generate the sequence of genes within the chromosome.

This property allows for customization of how the genes in the chromosome are created. By default, this can be set to any implementation of `ConstructorExecutor` that suits the specific requirements of the genetic algorithm.
