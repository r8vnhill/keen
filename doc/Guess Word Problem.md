Now, we're going to try and guess a given word, this problem is similar to the _One Max Problem_, 
but instead of counting the number of `true` values, we're going to count the number of characters
that match a given word. 

The _Word Guessing_ problem is a problem where the goal is to guess a word of known length by just
being able to ask "how many characters are in the correct position?".

# Setting up

Again, we will use the `engine` function to create an instance of the `Engine` class, but this time
we will use a ``CharChromosome`` instead of a ``BoolChromosome``. 

First, let us define the fitness function.
In this case, the fitness function will receive a ``Genotype`` composed of ``CharChromosome``s, and
compare each gene with the corresponding character in the given word. The fitness function will
return the number of characters that match the given word.

```kotlin
fun numberOfMatches(genotype: Genotype<Char>): Double {
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
```


Now we can define a basic engine:

```kotlin
val engine = engine(::numberOfMatches) {
    genotype = /* ... */
    populationSize = 1000
    alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
    limits = listOf(SteadyGenerations(10))
}
```

Here, we are creating a population of 1000 individuals, defining a ``Mutator`` and a 
``SinglePointCrossover`` as the alterers, and defining a terminating condition of 10 generations
without improvement.

The next thing we need is to define the genotype. 
In this case, we will create chromosomes of 10 genes (since Sopaipilla has 10 characters), each gene 
will be a character from the target word.

```kotlin
val engine = engine(::count) {
    genotype = genotype {
        chromosomes = listOf(CharChromosome.Builder(size = 10))
    }
    /* ... */
}
```

It's important to note that the ``CharChromosome`` will only generate alphanumeric characters, so
if you want to use a different set of characters, you will need to create a custom ``Chromosome``.

# Collecting the results

Lastly, we can evolve the population and collect the results.

```kotlin
engine.evolve()
engine.statistics.forEach {
    println(it)
}
```

This will print a result similar to the following:

```text
-------- Statistics Collector ---------
---------- Selection Times ------------
|--> Average: 75.55 ms
|--> Max: 185 ms
|--> Min: 41 ms
----------- Alteration Times ----------
|--> Average: 4.5 ms
|--> Max: 27 ms
|--> Min: 1 ms
---------- Evolution Results ----------
|--> Total time: 1801 ms
|--> Average generation time: 81.05 ms
|--> Max generation time: 214 ms
|--> Min generation time: 42 ms
|--> Generation: 20
|--> Steady generations: 10
|--> Fittest:  [ Sopaipilla ] 
|--> Best fitness: 10.0

Process finished with exit code 0

```

## Full code

```kotlin
fun numberOfMatches(genotype: Genotype<Char>): Double {
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
    val engine = engine(::numberOfMatches) {
        genotype = genotype {
            chromosomes = listOf(CharChromosome.Builder(size = 10))
        }
        populationSize = 1000
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(SteadyGenerations(10))
    }
    engine.evolve()
    engine.statistics.forEach { println(it) }
}
```