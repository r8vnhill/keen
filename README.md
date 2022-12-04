# Keen | Kotlin Genetic Algorithm Framework

![http://creativecommons.org/licenses/by/4.0/](https://i.creativecommons.org/l/by/4.0/88x31.png)

This work is licensed under a
[Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

_Keen_ is a Kotlin genetic algorithms' framework heavily inspired by 
[_Jenetics_](https://jenetics.io).

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
    implementation("com.github.r8vnhill:keen:1.0.0")
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
fun count(genotype: Genotype<Boolean>) = genotype.flatten().count { it }.toDouble()

fun main() {
    val engine = engine(::count, genotype {
        chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
    }) {
        populationSize = 500
        alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
        limits = listOf(GenerationCount(100), TargetFitness(20.0))
        statistics = listOf(StatisticCollector())
    }
    engine.run()
    println(engine.statistics[0])
}
```

#### Output

```text
------------ Statistics Collector -------------
-------------- Selection Times ----------------
|--> Offspring Selection
|   |--> Average: 1.5 ms
|   |--> Max: 16 ms
|   |--> Min: 0 ms
|--> Survivor Selection
|   |--> Average: 1.5 ms
|   |--> Max: 16 ms
|   |--> Min: 0 ms
--------------- Alteration Times --------------
|--> Average: 3.5625 ms
|--> Max: 42 ms
|--> Min: 0 ms
-------------- Evolution Results --------------
|--> Total time: 632 ms
|--> Average generation time: 19.375 ms
|--> Max generation time: 190 ms
|--> Min generation time: 3 ms
|--> Generation: 32
|--> Steady generations: 0
|--> Fittest: {  [ 1111|1111|1111|1111|1111 ]  -> 20.0 }
|--> Best fitness: 20.0
```

### Word Guessing Problem

The _Word Guessing_ problem is a problem where the goal is to guess a word of known length by just
being able to ask "how many characters are in the correct position?".

#### Implementation

```kotlin
private const val target = "Sopaipilla"

private fun matches(genotype: Genotype<Char>) = genotype.chromosomes.first().genes
    .filterIndexed { index, gene -> gene.dna == target[index] }
    .size.toDouble()

fun main() {
    val engine = engine(::matches, genotype {
        chromosomes = listOf(CharChromosome.Builder(10))
    }) {
        populationSize = 500
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(TargetFitness(target.length.toDouble()))
        statistics = listOf(StatisticPrinter(10), StatisticCollector())
    }
    val evolvedPopulation = engine.run()
    println(evolvedPopulation.generation)
    println(evolvedPopulation.best)
}
```

### Function Optimization Problem

Here we want to find the minimum of the function ``f(x) = cos(1 / 2 + sin(x)) * cos(x)``.

```kotlin
private fun fitnessFunction(x: Genotype<Double>): Double {
    val value = x.chromosomes.first().genes.first().dna
    return cos(0.5 + sin(value)) * cos(value)
}

fun main() {
    val engine = engine(::fitnessFunction, genotype {
        chromosomes = listOf(DoubleChromosome.Builder(1, 0.0..(2 * Math.PI)))
    }) {
        populationSize = 500
        optimizer = FitnessMinimizer()
        alterers = listOf(Mutator(0.03), MeanCrossover(0.6))
        statistics = listOf(StatisticPrinter(20), StatisticCollector())
    }
    val evolvedPopulation = engine.run()
    println(evolvedPopulation.generation)
    println(evolvedPopulation.best)
}
```
