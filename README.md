# Keen | Kotlin Evolutionary Computation Framework

[![](https://jitpack.io/v/r8vnhill/keen.svg)](https://jitpack.io/#r8vnhill/keen)
[![License](https://img.shields.io/badge/License-BSD_2--Clause-orange.svg)](https://opensource.org/licenses/BSD-2-Clause)

![Keen logo](logos/TransparentBg.png)

Keen is a framework for Kotlin that leverages evolutionary algorithms to solve optimization problems.
It harnesses the power and expressiveness of Kotlin to provide an
intuitive and efficient interface for researchers, scientists, and
developers alike.
With Keen, you can build and fine-tune complex evolutionary algorithms
with just a few lines of code.

Whether you are delving into genetic algorithms for the first time or are
an experienced researcher looking for a Kotlin-native tool, Keen offers a
streamlined experience without sacrificing depth or flexibility. Its
modular architecture means you can easily extend or modify components,
from selection and crossover methods to mutation and fitness evaluation.


## Installation

### Gradle Kotlin DSL

```kotlin
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
    /* ... */
}

dependencies {
    implementation("com.github.r8vnhill:keen:core:1.0.0")
    /* ... */
}
```

## Examples

The following examples only have the explanation of what's the problem to solve.
You can find the explanations of the code in the [wiki](https://github.com/r8vnhill/keen/wiki).

### One Max (Ones Counting) Problem

The _One Max_ problem is a well known problem widely used to exemplify the genetic algorithms'
capabilities.
The problem consists of maximizing the number of ones in a bit string.
The following example shows how to solve the _One Max_ problem with _Keen_.

#### Implementation

```kotlin
private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatMap().count { it }.toDouble()

fun main() {
    val engine = evolutionEngine(::count, genotype {
        chromosomeOf {
            booleans {
                size = 20
                trueRate = 0.15
            }
        }
    }) {
        populationSize = 500
        alterers += listOf(BitFlipMutator(individualRate = 0.5), SinglePointCrossover(chromosomeRate = 0.6))
        limits += listOf(MaxGenerations(100), TargetFitness(20.0))
        listeners += listOf(EvolutionSummary(), EvolutionPlotter())
    }
    engine.evolve()
    engine.listeners.forEach { it.display() }
}
```

#### Output

```text
------------ Evolution Summary ---------------
|--> Initialization time: 16 ms
------------- Evaluation Times ----------------
|--> Average: 0.23076923076923078 ms
|--> Max: 1 ms
|--> Min: 0 ms
-------------- Selection Times ----------------
|   |--> Offspring Selection
|   |   |--> Average: 0.6153846153846154 ms
|   |   |--> Max: 8 ms
|   |   |--> Min: 0 ms
|   |--> Survivor Selection
|   |   |--> Average: 0.0 ms
|   |   |--> Max: 0 ms
|   |   |--> Min: 0 ms
--------------- Alteration Times --------------
|--> Average: 3.3846153846153846 ms
|--> Max: 19 ms
|--> Min: 1 ms
-------------- Evolution Results --------------
|--> Total time: 113 ms
|--> Average generation time: 7.3076923076923075 ms
|--> Max generation time: 57 ms
|--> Min generation time: 2 ms
|--> Generation: 13
|--> Steady generations: 0
|--> Fittest: [1111 1111 1111 1111 1111]
|--> Best fitness: 20.0
```

![One Max Fitness Plot](docs/onemax.png)

## Acknowledgements

This project has benefited from the support provided by JetBrains. We're immensely grateful for their backing.

<div style="text-align: center;">
<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" alt="JetBrains Logo (Main) logo." width="100">
</div>

A big thank you to [JetBrains](https://jb.gg/OpenSourceSupport) for their open-source support.
