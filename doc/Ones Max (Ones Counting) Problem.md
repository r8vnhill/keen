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
val engine = engine(::count) {
    // We will fill this block later
}
```

The next thing we need is to define the genotype. 
The genotype is the representation of the individual in the population, i.e., a possible solution to
the problem.
The genotype is composed of chromosomes, and each chromosome is composed of genes.
The genotype is defined by the ``genotype`` function, which receives a configuration block.

```kotlin
val engine = engine(::count) {
    genotype = genotype {
        chromosomes = listOf(BoolChromosome.Builder(size = 24, truesProbability = 0.15))
    }    
}
```

Here, we defined a new ``Genotype`` composed of boolean chromosomes, each with 24 genes, and a 
trues-to-false ratio of 15% (i.e., 15% of the genes are true, and 85% are false).

The next thing we need to do is to define the population size, which is the number of individuals in
the population.
The population size is defined by the ``populationSize`` property.

```kotlin
val engine = engine(::count) {
    genotype = genotype { ... }
    populationSize = 500
}
```

Next, we can define the number of survivors, which is the number of individuals that will remain in
the population after each generation. Then, we can define the survivor selector, which is the 
strategy used to select the survivors from the population. The survivor selector is defined by the
``survivorSelector`` property.

```kotlin
val engine = engine(::count) {
    ...
    survivors = (populationSize * 0.2).toInt()
    survivorSelector = RouletteWheelSelector()
}
```

Here, we defined the number of survivors to be 20% of the population size, and we defined the
survivor selector to be the ``RouletteWheelSelector``, which is a simple survivor selector that
selects the survivors assigning a probability to each individual proportional to its fitness.

The next step is to define the alterers, which are the strategies used to alter the population in
each generation. The alterers are defined by the ``alterers`` property.

```kotlin
val engine = engine(::count) {
    ...
    alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
}
```

Here, we defined two alterers: a ``Mutator`` and a ``SinglePointCrossover``. 
The ``Mutator`` is a simple alterer that mutates the genes of the individuals in the population,
and the ``SinglePointCrossover`` is an alterer that performs a single point crossover between the
individuals in the population.

Lastly, we need to tell the engine under which conditions the evolution process should stop. This is
done by the ``limits`` property.

```kotlin
val engine = engine(::count) {
    ...
    limits = listOf(SteadyGenerations(20), GenerationCount(100))
}
```

Here, we defined two limits: a ``SteadyGenerations`` limit, which stops the evolution process when
the fitness of the population does not change for 20 generations, and a ``GenerationCount`` limit,
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
engine.evolve()
```

# Collecting the results

_Keen_ provides a collector for the statistics of the evolution process, which can be used to
get a summary of the results of the evolution.
Again, this is easily done by just calling:
```kotlin
engine.statistics.forEach {
    println(it)
}
```

This will print a result similar to the following:

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

## Full code

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