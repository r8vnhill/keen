//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Representation](index.md)

# Representation

interface [Representation](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [Verifiable](../../cl.ravenhill.keen.mixins/-verifiable/index.md), [FlatMappable](../../cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](index.md)&gt; , [Foldable](../../cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](index.md)&gt; 

Represents a generic representation in an evolutionary algorithm.

The `Representation` interface defines the structure for representing individuals in an evolutionary algorithm. It extends the [Verifiable](../../cl.ravenhill.keen.mixins/-verifiable/index.md) and [FlatMappable](../../cl.ravenhill.keen.mixins/-flat-mappable/index.md) interfaces, providing methods for verification and flat-mapping operations.

## Usage:

Use this interface to define the structure of representations in an evolutionary algorithm. A representation typically encapsulates the position of an individual in the search or solution space and provides methods for verification and flattening.

### Example:

Implementing a simple representation:

```kotlin
data class SimpleRepresentation(val genes: List<IntGene>) : Representation<Int, IntGene> {
    override val size = genes.size

    override fun verify() = genes.all { it.verify() }

    override fun flatten(): List<Int> = genes.flatMap { it.flatten() }
}
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../-feature/index.md). |

## Properties

| Name | Summary |
|---|---|
| [size](size.md) | [common]<br>abstract val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the representation, typically representing the number of features it contains. |

## Functions

| Name | Summary |
|---|---|
| [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md) | [common]<br>open fun &lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt; [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;<br>Applies a transformation function to each flattened element and returns a list of the results. |
| [flatten](../../cl.ravenhill.keen.mixins/-flat-mappable/flatten.md) | [common]<br>abstract fun [flatten](../../cl.ravenhill.keen.mixins/-flat-mappable/flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the structure into a list of elements. |
| [fold](../../cl.ravenhill.keen.mixins/-foldable/fold.md) | [common]<br>abstract fun &lt;[R](../../cl.ravenhill.keen.mixins/-foldable/fold.md)&gt; [fold](../../cl.ravenhill.keen.mixins/-foldable/fold.md)(initial: [R](../../cl.ravenhill.keen.mixins/-foldable/fold.md), operation: ([R](../../cl.ravenhill.keen.mixins/-foldable/fold.md), [T](index.md)) -&gt; [R](../../cl.ravenhill.keen.mixins/-foldable/fold.md)): [R](../../cl.ravenhill.keen.mixins/-foldable/fold.md)<br>Folds the elements of the structure from left to right, accumulating a result. |
| [foldRight](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md) | [common]<br>abstract fun &lt;[R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)&gt; [foldRight](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)(initial: [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md), operation: ([T](index.md), [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)) -&gt; [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)): [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)<br>Folds the elements of the structure from right to left, accumulating a result. |
| [verify](../../cl.ravenhill.keen.mixins/-verifiable/verify.md) | [common]<br>open fun [verify](../../cl.ravenhill.keen.mixins/-verifiable/verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the object. |
