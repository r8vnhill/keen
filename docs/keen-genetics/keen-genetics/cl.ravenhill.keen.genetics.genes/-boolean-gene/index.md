//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genes](../index.md)/[BooleanGene](index.md)

# BooleanGene

sealed interface [BooleanGene](index.md) : [Gene](../-gene/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](index.md)&gt; 

Represents a boolean gene in an evolutionary algorithm.

The `BooleanGene` sealed interface models a gene that holds a boolean value, which can be either [True](-true/index.md) or [False](-false/index.md). This interface extends the [Gene](../-gene/index.md) interface, inheriting the [flatMap](../../../../keen-genetics/cl.ravenhill.keen.genetics.genes/-boolean-gene/flat-map.md) function, which allows it to act as a monad. In Kotlin, a monad is a construct that follows the monad laws, providing composable and chainable operations on the contained value. Although Kotlin does not have native pattern matching, the `BooleanGene` provides an alternative approach through its sealed structure and methods like [toInt](to-int.md).

## Usage:

Implementations of `BooleanGene` are used in genetic algorithms where binary decisions or states are needed. The interface provides essential methods for value generation, duplication, and transformation. The sealed nature of the interface ensures that the possible values are restricted to `True` and `False`.

### Example 1: Creating and Using Boolean Genes

```kotlin
val gene = BooleanGene.pure(true)
val mutatedGene = gene.mutate()
println(mutatedGene.value) // Output: either `True` or `False`
println(mutatedGene.toInt()) // Output: 1 if `True`, 0 if `False`
```

### Example 2: Converting Between Boolean and Integer Values

```kotlin
val geneFromInt = BooleanGene.fromInt(1)
println(geneFromInt.value) // Output: `True`

val geneFromBoolean = BooleanGene.from(false)
println(geneFromBoolean.value) // Output: `False`
```

## Monadic Properties:

Since `BooleanGene` inherits the `bind` method from the `Gene` interface, it follows the monadic structure:

- 
   **Pure Function**: The `pure` function allows you to wrap a raw boolean value into a `BooleanGene` instance, adhering to the monad's `unit` or `pure` concept.
- 
   **Monad Laws**: The `BooleanGene` implementation respects the monad laws (left identity, right identity, and associativity), enabling predictable and composable operations on its value.

## Kotlin's Lack of Pattern Matching:

Kotlin does not support traditional pattern matching as seen in languages like Scala or Haskell. However, the `BooleanGene` interface circumvents this limitation by using sealed classes and methods like `toInt()` to provide similar functionality. The `if-else` structure within the [copyWithValue](copy-with-value.md) method and the `True` and `False` data objects act as alternatives to pattern matching.

#### Inheritors

| |
|---|
| [True](-true/index.md) |
| [False](-false/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |
| [False](-false/index.md) | [common]<br>data object [False](-false/index.md) : [BooleanGene](index.md)<br>Represents the `False` state of the boolean gene. |
| [True](-true/index.md) | [common]<br>data object [True](-true/index.md) : [BooleanGene](index.md)<br>Represents the `True` state of the boolean gene. |

## Properties

| Name | Summary |
|---|---|
| [generator](generator.md) | [common]<br>open override val [generator](generator.md): ([Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>A function that generates new boolean values for the gene, typically used in mutation operations. |
| [value](../-gene/index.md#276755286%2FProperties%2F-1476930196) | [common]<br>abstract val [value](../-gene/index.md#276755286%2FProperties%2F-1476930196): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Functions

| Name | Summary |
|---|---|
| [copyWithValue](copy-with-value.md) | [common]<br>open override fun [copyWithValue](copy-with-value.md)(value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BooleanGene](index.md)<br>Duplicates the gene with a new specified boolean value. |
| [flatMap](-false/index.md#600781797%2FFunctions%2F-1476930196) | [common]<br>open fun [flatMap](-false/index.md#600781797%2FFunctions%2F-1476930196)(f: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [BooleanGene](index.md)): [BooleanGene](index.md) |
| [map](-false/index.md#94155127%2FFunctions%2F-1476930196) | [common]<br>open override fun [map](-false/index.md#94155127%2FFunctions%2F-1476930196)(transform: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BooleanGene](index.md) |
| [mutate](../-gene/mutate.md) | [common]<br>open fun [mutate](../-gene/mutate.md)(random: [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) = Domain.random): [BooleanGene](index.md)<br>Mutates the gene by generating a new value using the `generator` function. |
| [toInt](to-int.md) | [common]<br>open fun [toInt](to-int.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Converts the boolean gene's value to an integer. |
| [toList](../-gene/index.md#-321272454%2FFunctions%2F-1476930196) | [common]<br>open fun [toList](../-gene/index.md#-321272454%2FFunctions%2F-1476930196)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [verify](../-gene/index.md#629456716%2FFunctions%2F-1476930196) | [common]<br>open fun [verify](../-gene/index.md#629456716%2FFunctions%2F-1476930196)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
