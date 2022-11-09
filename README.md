# Keen | Kotlin Genetic Algorithm Framework

![http://creativecommons.org/licenses/by/4.0/](https://i.creativecommons.org/l/by/4.0/88x31.png)

This work is licensed under a
[Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

_Keen_ is a Kotlin genetic algorithms' framework heavily inspired by 
[_Jenetics_](https://jenetics.io).

## Examples

### One Max (Ones Counting) Problem

The _One Max_ problem is a well known problem widely used to exemplify the genetic algorithms'
capabilities. 
The problem consists of maximizing the number of ones in a bit string. 
The following example shows how to solve the _One Max_ problem with _Keen_.

#### Implementation

```kotlin
fun count(genotype: Genotype<Boolean>): Double =
    genotype.chromosomes[0].genes.count { it.dna }.toDouble()

fun main() {
    val engine = engine(::count) {
        genotype = genotype {
            chromosomes = listOf(BoolChromosome.Builder(24, 0.15))
        }
        populationSize = 500
        survivors = (populationSize * 0.2).toInt()
        survivorSelector = RouletteWheelSelector()
        alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(20), GenerationCount(100))
    }
    engine.evolve()
    engine.statistics.forEach {
        println(it)
    }
}
```

#### Output

```text
---------- Selection Times ------------
|--> Average: 77.34375 ms
|--> Max: 254 ms
|--> Min: 26 ms
----------- Alteration Times ----------
|--> Average: 5.65625 ms
|--> Max: 45 ms
|--> Min: 0 ms
---------- Evolution Results ----------
|--> Total time: 3182 ms
|--> Average generation time: 84.21875 ms
|--> Max generation time: 261 ms
|--> Min generation time: 28 ms
|--> Generation: 32
|--> Steady generations: 20
|--> Fittest:  [ 11111111|11111111|11111111 ] 
|--> Best fitness: 24.0
```

### Word Guessing Problem

The _Word Guessing_ problem is a problem where the goal is to guess a word of known length by just
being able to ask "how many characters are in the correct position?".

#### Implementation

```kotlin
fun fitnessFn(genotype: Genotype<Char>): Double {
    val target = "Sopaipilla"
    var fitness = 0.0
    genotype.chromosomes.forEach { chromosome ->
        chromosome.genes.forEachIndexed { idx, gene ->
            if (gene.dna == target[idx]) {
                fitness++
            }
        }
    }
    return fitness
}

fun main() {
    val engine = engine(::fitnessFn) {
        genotype = genotype {
            chromosomes = listOf(CharChromosome.Builder(10))
        }
        populationSize = 1000
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(10))
    }
    engine.evolve()
    println(engine.statistics)
}
```
