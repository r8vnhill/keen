The _One Max_ problem is a well known problem widely used to exemplify the genetic algorithms'
capabilities.
The problem consists of maximizing the number of ones in a bit string.
The following example shows how to solve the _One Max_ problem with _Keen_.

# Setting up

To solve the problem, we will be using the evolution ``Engine``; this is the central entity of the
library. The ``Engine`` is responsible for the evolution process, and it is configured by the
``engine`` function.

The ``engine`` function receives a fitness function and a configuration block.
We can define a reference to the fitness function using the ``::`` operator, which is called the
"function reference" operator. The fitness function is a function that receives a ``Genotype`` (an
"individual" of the population), and returns a ``Double`` value (the fitness of the genotype).

```kotlin
fun count(genotype: Genotype<Boolean>): Double =
    genotype.chromosomes[0].genes.count { it.dna }.toDouble()

val engine = engine(::count, /*...*/) {
    // We will fill this block later      
}
```

The next thing we need is to define the genotype.
The genotype is the representation of the individual in the population, i.e., a possible solution to
the problem.
The genotype is composed of chromosomes, and each chromosome is composed of genes.
The genotype is defined by the ``genotype`` function, which receives a configuration block.

```kotlin
val engine = engine(::count, genotype {
    chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
}) {
    // We will fill this block later          
}
```

Here, we defined a new ``Genotype`` composed of boolean chromosomes, each with 20 genes, and a
trues-to-false ratio of 15% (i.e., 15% of the genes are true, and 85% are false).

The next thing we need to do is to define the population size, which is the number of individuals in
the population.
The population size is defined by the ``populationSize`` property.

```kotlin
val engine = engine(::count, genotype {
    chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
}) {
    populationSize = 500
}
```

Next, we can define the algorithm that will select the offspring and the survivors.
Here we use a Tournament Selection, which is a selection algorithm that creates a "Tournament" of
random individuals and selects the best one of the participants.
If no ``selector`` is specified, a Tournament Selection with 3 participants is used by default,
generally more than 3 participants is not necessary, and it can even be detrimental to the evolution
process.
In this example we will use a Tournament Selection with 2 participants since the problem to solve is
very simple.

```kotlin
val engine = engine(::count, genotype {
    chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
}) {
    populationSize = 500
    selector = TournamentSelector(2)
}
```

The next step is to define the alterers, which are the strategies used to alter the population in
each generation.
The alterers are defined by the ``alterers`` property.

```kotlin
val engine = engine(::count, genotype { /* ... */ }) {
    /* ... */
    alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
}
```

Here, we defined two alterers: a ``Mutator`` and a ``SinglePointCrossover``.
The ``Mutator`` is a simple alterer that mutates the genes of the individuals in the population,
and the ``SinglePointCrossover`` is an alterer that performs a single point crossover between the
individuals in the population.

Lastly, we need to tell the engine under which conditions the evolution process should stop.
This is done by the ``limits`` property.

```kotlin
val engine = engine(::count, genotype { /* ... */ }) {
    /* ... */
    limits = listOf(GenerationCount(100), TargetFitness(20.0))
}
```

Here, we defined two limits: a ``TargetFitness`` limit, which stops the evolution process when
the fitness of the population reaches a certain value, and a ``GenerationCount`` limit,
which stops the evolution process after 100 generations.

# Running the engine

Now that we have defined the engine, we can run it, we will call this "evolution".

The evolution works as follows:

```kotlin
// Not the actual implementation!
fun evolve() {
    createPopulation()
    while (limits.none { it(this) }) { // While none of the limits are met
        population = select(populationSize)     // Select the population
        population = alter(population)          // Alter the population
    }
}
```

This allows us to easily perform the evolution process by just calling:

```kotlin
engine.run()
```

# Collecting the results

_Keen_ provides a collector for the statistics of the evolution process, which can be used to
get a summary of the results of the evolution.
We can add a collector to the engine by using the ``statistics`` property.

```kotlin
val engine = engine(::count, genotype { /* ... */ }) {
    /* ... */
    statistics = listOf(StatisticCollector())
}
```

And then we can get the statistics by simply calling:

```kotlin
engine.statistics.forEach {
    println(it)
}
```

This will print a result similar to the following:

```text
------------ Statistics Collector -------------
-------------- Selection Times ----------------
|--> Offspring Selection
|   |--> Average: 4.260869565217392 ms
|   |--> Max: 26 ms
|   |--> Min: 1 ms
|--> Survivor Selection
|   |--> Average: 3.869565217391304 ms
|   |--> Max: 24 ms
|   |--> Min: 1 ms
--------------- Alteration Times --------------
|--> Average: 4.6521739130434785 ms
|--> Max: 41 ms
|--> Min: 1 ms
-------------- Evolution Results --------------
|--> Total time: 492 ms
|--> Average generation time: 20.695652173913043 ms
|--> Max generation time: 271 ms
|--> Min generation time: 3 ms
|--> Generation: 23
|--> Steady generations: 0
|--> Fittest: {  [ 1111|1111|1111|1111|1111 ]  -> 20.0 }
|--> Best fitness: 20.0
```

## Full code

```kotlin
fun count(genotype: Genotype<Boolean>): Double =
    genotype.chromosomes[0].genes.count { it.dna }.toDouble()

fun main() {
    val engine = engine(::count, genotype {
        chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
    }) {
        populationSize = 500
        selector = TournamentSelector(sampleSize = 2)
        alterers = listOf(Mutator(probability = 0.55), SinglePointCrossover(probability = 0.06))
        limits = listOf(GenerationCount(100), TargetFitness(20.0))
        statistics = listOf(StatisticCollector())
    }
    engine.run()
    println(engine.statistics[0])
}
```

# References

- “One Max Problem — DEAP 1.3.3 Documentation.” Accessed December 1, 2022. 
  https://deap.readthedocs.io/en/master/examples/ga_onemax.html.
