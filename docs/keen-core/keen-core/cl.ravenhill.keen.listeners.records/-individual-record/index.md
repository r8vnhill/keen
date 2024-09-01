//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[IndividualRecord](index.md)

# IndividualRecord

data class [IndividualRecord](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val representation: [R](index.md), val fitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html))

Data class representing an individual record in the Keen evolutionary computation framework.

The `IndividualRecord` class encapsulates the representation and fitness of an individual. It provides utility functions to convert between `Individual` and `IndividualRecord` objects.

## Usage:

This class is used to store and manage the representation and fitness of individuals in a more lightweight form compared to the `Individual` class. It also provides conversion functions to and from `Individual` objects.

### Example 1: Creating an IndividualRecord

```kotlin
val representation = MyRepresentation(...)
val fitness = 1.0
val individualRecord = IndividualRecord(representation, fitness)
```

### Example 2: Converting to Individual

```kotlin
val individual = individualRecord.toIndividual()
```

### Example 3: Creating IndividualRecord from Individual

```kotlin
val individual = Individual(representation, fitness)
val individualRecord = IndividualRecord.fromIndividual(individual)
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
| [IndividualRecord](-individual-record.md) | [common]<br>constructor(representation: [R](index.md), fitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html))<br>Creates an instance of `IndividualRecord` with the specified representation and fitness. |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [fitness](fitness.md) | [common]<br>val [fitness](fitness.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The fitness value of the individual. |
| [representation](representation.md) | [common]<br>val [representation](representation.md): [R](index.md)<br>The representation of the individual. |

## Functions

| Name | Summary |
|---|---|
| [toIndividual](to-individual.md) | [common]<br>fun [toIndividual](to-individual.md)(): [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>Converts this record to an `Individual` object. |
