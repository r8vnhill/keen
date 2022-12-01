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
private const val TARGET = "Sopaipilla"

private fun matches(genotype: Genotype<Char>) = genotype.chromosomes.first().genes
    .filterIndexed { index, gene -> gene.dna == TARGET[index] }
    .size.toDouble()
```


Now we can define a basic engine:

```kotlin
val engine = engine(::matches, genotype {/* ... */}) {
    populationSize = 500
    survivorSelector = RouletteWheelSelector()
    alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
    limits = listOf(TargetFitness(target.length.toDouble()))
    statistics = listOf(StatisticPrinter(10))
}
```

Here, we are creating a population of 500 individuals, defining a ``Mutator`` and a 
``SinglePointCrossover`` as the alterers, and defining a terminating condition of reaching a target
fitness of the length of the word (meaning we found the word).
We also defined a survivor selector of ``RouletteWheelSelector``, this means that the survivor 
population will be selected using a fitness proportionate selection, whereas the offspring 
population will be selected with the default ``TouramentSelector``.

We also added a ``StatisticPrinter`` to print the statistics every 10 generations so we can see the
progress of the algorithm.

The next thing we need is to define the genotype. 
In this case, we will create chromosomes of 10 genes (since Sopaipilla has 10 characters), each gene 
will be a character from the target word.

```kotlin
val engine = engine(::matches, genotype {
    chromosomes = listOf(CharChromosome.Builder(10))
}) {
    /* ... */
}
```

It's important to note that the ``CharChromosome`` will only generate alphanumeric characters, so
if you want to use a different set of characters, you will need to create a custom ``Chromosome``.

# Collecting the results

The ``run`` method returns an ``EvolutionResult`` object, which contains the information of the 
population at the end of the evolution.
We can use this object to get the best individual of the population, and print the word that it
represents.

```kotlin
val evolvedPopulation = engine.run()
println("Solution found in ${evolvedPopulation.generation} generations")
println("Solution: ${evolvedPopulation.best?.genotype}")
println("With fitness: ${evolvedPopulation.best?.fitness}")
```

Which will print something like this:

```
Solution found in 453 generations
Solution:  [ Sopaipilla ] 
With fitness: 10.0
```

## Full code

```kotlin
private const val TARGET = "Sopaipilla"

private fun matches(genotype: Genotype<Char>) = genotype.chromosomes.first().genes
    .filterIndexed { index, gene -> gene.dna == TARGET[index] }
    .size.toDouble()

fun main() {
    val engine = engine(::matches, genotype {
        chromosomes = listOf(CharChromosome.Builder(10))
    }) {
        populationSize = 500
        survivorSelector = RouletteWheelSelector()
        alterers = listOf(Mutator(0.03), SinglePointCrossover(0.06))
        limits = listOf(TargetFitness(TARGET.length.toDouble()))
        statistics = listOf(StatisticPrinter(10), StatisticCollector())
    }
    val evolvedPopulation = engine.run()
    println("Solution found in ${evolvedPopulation.generation} generations")
    println("Solution: ${evolvedPopulation.best?.genotype}")
    println("With fitness: ${evolvedPopulation.best?.fitness}")
}
```