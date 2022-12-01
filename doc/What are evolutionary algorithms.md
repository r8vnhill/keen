An _Evolutionary Algorithm_ (EA) is a _Machine Learning_ algorithm that is inspired by the process
of _Natural Selection_.
EA is a _Stochastic_ algorithm, which means that it uses _Randomness_ to find a solution to a given
optimization problem.

The EA is composed of a _Population_ of _Individuals_, each individual is a possible solution to the
problem, and each individual is composed of _Genes_.
These algorithms often perform well in approximating solutions to a wide range of problems because
they do not make any assumptions about the problem (which make them a kind of _Unsupervised 
Learning_ algorithm), and they are able to find solutions to problems that are not easily solved by 
other approaches.

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

## Ones Max (Ones Counting) Problem

The _Ones Max_ problem is a simple problem that is used to demonstrate the basic concepts of
Genetic Algorithms.
The problem is to find the individual with the highest fitness, which is the individual with the
highest number of ones.

The fitness function is very simple, it receives an individual and returns the number of ones in
the individual; note that this is equivalent to maximizing the integer value of a binary string.


