In nature, the **evolutionary process** occurs when the following four conditions are satisfied:

- An entity has the ability to reproduce itself.
- There is a population of these entities.
- There is variety in the population.
- Some entities in the population are more fit for survival than others.

This is the concept of _survival of the fittest_ introduced by Charles Darwin in his book _On the
Origin of Species_.
The fittest entities are those that are better adapted to their environment.
The fittest entities are more likely to survive and reproduce, and the less fit are more likely to
die.
This process is called _natural selection_.

An _Evolutionary Algorithm_ (EA) is a _Machine Learning_ algorithm that is inspired by the process
of _Natural Selection_.
EA is a _Stochastic_ algorithm, which means that it uses _Randomness_ to find a solution to a given
optimization problem.

The EA is composed of a _Population_ of _candidate solutions_ to the problem, usually called 
_Individuals_, and a fitness function that determines the quality of the solutions.
Evolution of the population then takes place after the repeated application of operators inspired by
biological evolution, such as _Reproduction_, _Recombination_ and _Mutation_.

# Genetic Algorithms

The _Genetic Algorithm_ (GA) is a specific EA that uses _Genetic Operators_ to evolve the
population.
The GA is composed of a _Population_ of _Individuals_ (or _Phenotypes_), each individual is a
possible solution to the problem, and each individual is composed of _Genes_.

A key concept of the GA is the _Fitness Function_, which is a function that receives an individual
and returns a _Fitness Value_.
The fitness value is a measure of how good the individual is at solving the problem.
The GA uses the fitness function to discriminate between individuals, and it's main goal is to
evolve the population to find individuals with the highest fitness values (highest in this context
doesn't necessarily mean the biggest value, it can also mean the smallest value, or the value that
is closest to a target value; it depends on the problem).

A classical GA is composed of the following steps:

1. _Initialization_: The population is initialized with random individuals.
2. _Evaluation_: The fitness of each individual is evaluated.
3. _Selection_: The individuals are selected to be the parents of the next generation.
4. _Crossover_: The parents are combined to create the offspring.
5. _Mutation_: The offspring are altered to introduce diversity.
6. _Termination_: The algorithm is terminated if a stopping criterion is met.
7. _Repeat_: The algorithm returns to step 2.
8. _Solution_: The best individual is returned.

_Note:_ Steps 4 and 5 are often interchangeable.

## Ones Max (Ones Counting) Problem

The _Ones Max_ problem is a simple problem that is used to demonstrate the basic concepts of
Genetic Algorithms.
The problem is to find the individual with the highest fitness, which is the individual with the
highest number of ones.

### Fitness Function

The fitness function is very simple, it receives an individual and returns the number of ones in
the individual.

![](https://raw.githubusercontent.com/r8vnhill/keen/master/doc/one_max_fitness.png)

### Initialization

The execution of a GA starts with an effort to learn something about the environment by testing a
set of random points in the search space.
In particular, this is done by creating a population of random individuals and evaluating their
fitness; let's assume an initial population of 4 individuals.

We can now evaluate the fitness of each individual using the fitness function _f_:

**Generation 0:**

| Individual | Fitness |
|------------|---------|
| 0010       | 1       |
| 0110       | 2       |
| 0000       | 0       |
| 1000       | 1       |

- **Worst:** 0 (0000)
- **Best:** 2 (0110)
- **Average:** 1
- **Total**: 4

Now, the algorithm knows the fitness of 4 particular points in the search space.
The only information that the algorithm handles is the observed fitness values of the individuals
currently in the population.

### Selection

The next step is to select the individuals that will be the parents of the next generation.
The selection process is a stochastic process, which means that it uses randomness to select the
parents.
The selection process is usually implemented using a _Tournament Selection_, but for the sake of
simplicity, we will use a _Roulette Wheel Selection_.
A _Roulette Wheel Selection_ is a selection process that uses a _Fitness Proportionate Selection_,
where each individual has a chance of being selected proportional to its fitness value.
The higher the fitness value, the higher the chance of being selected.
In this case, the total fitness of the population is 4, and the fitness of the best individual is 2,
so the probability of being selected as a parent is 2/4.
This means that it will probably be selected as a parent, but it's not guaranteed.

### Crossover

The next step is to combine the parents to create the offspring.
This process is usually called _crossover_, and it is a way to test new points in the search space.
The simplest crossover strategy is the _Single-Point Crossover_, where a random point is selected in
the individual, and the genes are swapped between the parents.
Let's say we select the second and the first individual as the parents, and we select the 2nd gene
as the crossover point; the offspring produced will have the first two genes from the first parent
(01) and the last two genes from the second parent (10), resulting in the offspring 0110.
If we now evaluate the fitness of the offspring, we will get 2, which is higher than the fitness of
one of the parents.

We can now repeat this process until we reach the desired number of offspring,

| Parent 1 | Parent 2 | Cut Point | Offspring | Fitness |
|----------|----------|-----------|-----------|---------|
| 0110     | 0010     | 2         | 0110      | 2       |
| 0110     | 0110     | 3         | 0110      | 2       |
| 0110     | 0000     | 1         | 0100      | 1       |
| 0110     | 1000     | 4         | 0110      | 2       |

- **Worst:** 1 (0100)
- **Best:** 2 (0110)
- **Average:** 1.75
- **Total**: 7

We can see that the average fitness of the offspring is higher than the average fitness of the
parents, which means that the crossover process is working as expected.

### Mutation

The next step is to alter the offspring to introduce diversity.
This process is usually called _mutation_, and it is a way to avoid getting stuck in local optima, 
in fact, we can see that the best individual of the previous generation is the same as the best
individual of the current generation.

The simplest mutation strategy is the _Bit Flip Mutation_, where a random gene is selected and its
value is flipped.
This operation is applied with a certain probability, which is called the _Mutation Rate_, which
is usually a small value, like 0.01.
For this example, let's assume that the mutation rate is 0.25, which means that each offspring has a
25% chance of being mutated.

Applying this operation, we can get the following offspring:

| Original | Mutated | Fitness |
|----------|---------|---------|
| 0110     | 0110    | 2       |
| 0110     | 0011    | 2       |
| 0100     | 1000    | 1       |
| 0110     | 0100    | 1       |

It is clear that the mutation process reduced the average fitness of the offspring (this happened 
because we assumed a high mutation rate), but it also introduced diversity, which will be important
when we perform the crossover process on the next generation.

### Termination

The next step is to check if the algorithm should be terminated.
This is done by checking if a stopping criterion is met.
Once the termination criterion is checked, the algorithm returns to the selection step.

# References

1. Koza, John R. Genetic Programming. 1: On the Programming of Computers by Means of Natural
   Selection. Complex Adaptive Systems. Cambridge, Mass.: MIT Press, 1992.

