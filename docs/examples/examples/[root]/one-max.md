//[examples](../../index.md)/[[root]](index.md)/[oneMax](one-max.md)

# oneMax

[common]\
suspend fun [oneMax](one-max.md)()

Runs a genetic algorithm to solve the OneMax problem.

## Problem Statement:

Given a binary string x = (x1, x2, ..., xn) of length n, where each xi is either 0 or 1, the goal of the OneMax problem is to maximize the sum of the elements in x, represented by the fitness function:

f(x) = x1 + x2 + ... + xn

The objective is to find the binary string x that maximizes f(x). The optimal solution occurs when all bits are set to 1, meaning f(x) = n.

In this implementation, the length of the binary string n is set to 50.

## Description:

The `oneMax` function sets up and runs a genetic algorithm to solve the OneMax problem. The algorithm evolves a population of individuals, each represented by a binary string (genotype), to maximize the fitness function f(x).

### Genetic Algorithm Configuration:

- 
   **Genotype**: The genotype is a binary string of length 50, with an initial `true` (or 1) rate of 0.15.
- 
   **Population Size**: The population consists of 500 individuals.
- 
   **Parent and Survivor Selection**: A tournament selection strategy is used for both selecting parents and survivors.
- 
   **Alterers**: The algorithm applies a `BitFlipMutator` to mutate individual genes and a `UniformCrossover` with a chromosome rate of 0.6 for recombination.
- 
   **Fitness Target**: The algorithm's evolution process is limited by a target fitness of 50, corresponding to the maximum possible sum of the binary string (i.e., all bits are 1).
- 
   **Listeners**: An `EvolutionSummary` listener is added to monitor and display the progress of the evolution process.

## Execution:

The algorithm evolves the population until one of the individuals reaches the target fitness f(x) = 50, where all bits in the binary string are set to 1. After the evolution process is complete, the summary of the evolution is displayed.

## Example Usage:

```kotlin
fun main() {
    runBlocking {
       oneMax()
    }
}
```
