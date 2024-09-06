//[examples](../../index.md)/[[root]](index.md)/[oneMaxMinimal](one-max-minimal.md)

# oneMaxMinimal

[common]\
suspend fun [oneMaxMinimal](one-max-minimal.md)()

Implements the OneMax problem using a genetic algorithm with minimal configurations.

The `oneMaxMinimal` function demonstrates how to solve the OneMax problem—a classic optimization problem in evolutionary computation—with minimal configuration. The goal of the OneMax problem is to maximize the number of `true` values (or 1-bits) in a binary string. The optimal solution is a string where all bits are set to `true`.

## Problem Statement:

The OneMax problem can be formally stated as follows: Given a binary string x = (x1, x2, ..., xn) of length n, where each xi is either 0 or 1, the objective is to maximize the sum of the elements in x, represented by the fitness function:

f(x) = x1 + x2 + ... + xn

The optimal solution occurs when all bits are set to 1, meaning f(x) = n. In this implementation, the length of the binary string n is set to 50.

## Description:

The `oneMaxMinimal` function sets up and runs a genetic algorithm to solve the OneMax problem with a simple and minimal configuration. The algorithm evolves a population of individuals, each represented by a binary string (genotype), to maximize the fitness function f(x).

### Genetic Algorithm Configuration:

- 
   **Genotype**: A binary string of length 50.
- 
   **Alterers**: The algorithm applies a `BitFlipMutator` to mutate individual genes and a `UniformCrossover`.
- 
   **Fitness Target**: The algorithm's evolution process is limited by a target fitness of 50, corresponding to the maximum possible sum of the binary string (i.e., all bits are 1).
- 
   **Listeners**: An `EvolutionSummary` listener is added to monitor the progress of the evolution, and an `EvolutionPlotter` listener is added to visualize the fitness over generations.

### Example Usage:

The `oneMaxMinimal` function can be executed as follows:

```kotlin
fun main() {
    runBlocking {    // Not required for JS
        oneMaxMinimal()
    }
}
```

## Execution:

The genetic algorithm evolves the population until one of the individuals reaches the target fitness f(x) = 50, where all bits in the binary string are set to 1. After the evolution process is complete, the summary of the evolution and a plot of the fitness progression are displayed.

## Notes:

- 
   This example demonstrates the power and flexibility of the genetic algorithm DSL in Keen, enabling the creation of sophisticated evolutionary algorithms with minimal code.
- 
   Since most configurations are set to default values, this example is not optimized for performance but serves as a starting point for more complex evolutionary algorithms. For more advanced configurations, refer to the [oneMax](one-max.md) function.
