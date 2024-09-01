//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[Individual](index.md)

# Individual

data class [Individual](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val representation: [R](index.md), val fitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = Double.NaN) : [Verifiable](../../cl.ravenhill.keen.mixins/-verifiable/index.md), [FlatMappable](../../cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](index.md)&gt; , [Foldable](../../cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](index.md)&gt; 

Represents an individual in an evolutionary algorithm.

The `Individual` class encapsulates the representation and fitness of an individual in an evolutionary process. It implements the [Verifiable](../../cl.ravenhill.keen.mixins/-verifiable/index.md), [FlatMappable](../../cl.ravenhill.keen.mixins/-flat-mappable/index.md), and [Foldable](../../cl.ravenhill.keen.mixins/-foldable/index.md) interfaces, allowing it to be verified for consistency, support flat-mapping operations, and perform fold operations.

## Usage:

Use this class to represent individuals in an evolutionary algorithm, where each individual has a representation indicating its position in the search or solution space, along with a fitness value that reflects its quality or suitability in the evolutionary context.

### Example (requires the `keen-genetics` module):

```kotlin
val gene1 = IntGene(1, 0..10)
val gene2 = IntGene(2, 0..10)
val chromosome = IntChromosome(gene1, gene2)
val representation = Genotype(chromosome)
val individual = Individual(representation, fitness = 42.0)

println(individual) // Output: Genotype([IntChromosome([IntGene(1), IntGene(2)])]) -> 42.0
println(individual.verify()) // Output: true
println(individual.flatten()) // Output: [1, 2]
println(individual.isEvaluated()) // Output: true
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

## Constructors

| | |
|---|---|
| [Individual](-individual.md) | [common]<br>constructor(representation: [R](index.md), fitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) = Double.NaN)<br>Creates an instance of `Individual` with the specified representation and fitness. |

## Properties

| Name | Summary |
|---|---|
| [fitness](fitness.md) | [common]<br>val [fitness](fitness.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The fitness value of the individual, indicating its quality, defaulting to `Double.NaN` if not evaluated. |
| [representation](representation.md) | [common]<br>val [representation](representation.md): [R](index.md)<br>The position of the individual in the search or solution space. |
| [size](size.md) | [common]<br>val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the individual's representation, lazily computed. |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if this individual is equal to another object. |
| [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md) | [common]<br>open fun &lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt; [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;<br>Applies a transformation function to each flattened element and returns a list of the results. |
| [flatten](flatten.md) | [common]<br>open override fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the individual's representation into a list of elements. |
| [fold](fold.md) | [common]<br>open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)<br>Folds the values in the individual's representation from left to right, accumulating a result. |
| [foldRight](fold-right.md) | [common]<br>open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)<br>Folds the values in the individual's representation from right to left, accumulating a result. |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Computes the hash code for this individual. |
| [isEvaluated](is-evaluated.md) | [common]<br>fun [isEvaluated](is-evaluated.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the individual's fitness has been evaluated. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the individual. |
| [verify](verify.md) | [common]<br>open override fun [verify](verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the consistency and validity of the individual's representation and fitness. |
