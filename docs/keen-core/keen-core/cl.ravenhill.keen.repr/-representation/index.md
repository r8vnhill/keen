//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Representation](index.md)

# Representation

interface [Representation](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [Verifiable](../../cl.ravenhill.keen.mixins/-verifiable/index.md), [FlatMappable](../../cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](index.md)&gt; , [Foldable](../../cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](index.md)&gt; , [Mappable](../../cl.ravenhill.keen.mixins/-mappable/index.md)&lt;[T](index.md)&gt; 

Represents a generic structure for individuals in an evolutionary algorithm.

The `Representation` interface defines the contract for representing individuals, encapsulating their structure within the search or solution space. It provides core methods for operations such as verification, flat-mapping, folding, and mapping, making it adaptable for use in various evolutionary computations.

This interface also allows representations to be manipulated by removing or extracting a subset of elements, using [drop](drop.md) and [take](take.md) methods, respectively. Additionally, it supports methods for verifying the correctness of the representation.

## Usage:

Implement this interface to define the structure of a representation in an evolutionary algorithm. It is expected to handle features (or components) of type [F](index.md), and should provide mechanisms for validation, transformation, and traversal of the representation.

### Example: Implementing a simple representation

```kotlin
data class SimpleRepresentation(val genes: List<IntGene>) : Representation<Int, IntGene> {
    override val size = genes.size

    override fun verify() = genes.all { it.verify() }

    override fun flatten(): List<Int> = genes.flatMap { it.flatten() }

    override fun drop(n: Int): Either<Exception, SimpleRepresentation> =
        if (n <= size) SimpleRepresentation(genes.drop(n)).right() else Exception("Drop out of bounds").left()

    override fun take(n: Int): Either<Exception, SimpleRepresentation> =
        if (n <= size) SimpleRepresentation(genes.take(n)).right() else Exception("Take out of bounds").left()

    // ... other methods and properties ...
}
```

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features. |
| F | The type of feature within the representation, which must extend [Feature](../-feature/index.md). |

## Properties

| Name | Summary |
|---|---|
| [size](size.md) | [common]<br>abstract val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the representation, representing the number of components or features it contains. |

## Functions

| Name | Summary |
|---|---|
| [drop](drop.md) | [common]<br>abstract fun [drop](drop.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;[Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), [Representation](index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;<br>Returns a new representation by removing the first [n](drop.md) elements. |
| [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md) | [common]<br>open fun &lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt; [flatMap](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.mixins/-flat-mappable/flat-map.md)&gt;<br>Applies a transformation function to each flattened element and returns a list of the results. |
| [flatten](../../cl.ravenhill.keen.mixins/-flat-mappable/flatten.md) | [common]<br>abstract fun [flatten](../../cl.ravenhill.keen.mixins/-flat-mappable/flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the structure into a list of elements. |
| [fold](../../cl.ravenhill.keen.mixins/-foldable/fold.md) | [common]<br>abstract fun &lt;[R](../../cl.ravenhill.keen.mixins/-foldable/fold.md)&gt; [fold](../../cl.ravenhill.keen.mixins/-foldable/fold.md)(initial: [R](../../cl.ravenhill.keen.mixins/-foldable/fold.md), operation: ([R](../../cl.ravenhill.keen.mixins/-foldable/fold.md), [T](index.md)) -&gt; [R](../../cl.ravenhill.keen.mixins/-foldable/fold.md)): [R](../../cl.ravenhill.keen.mixins/-foldable/fold.md)<br>Folds the elements of the structure from left to right, accumulating a result. |
| [foldRight](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md) | [common]<br>abstract fun &lt;[R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)&gt; [foldRight](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)(initial: [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md), operation: ([T](index.md), [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)) -&gt; [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)): [R](../../cl.ravenhill.keen.mixins/-foldable/fold-right.md)<br>Folds the elements of the structure from right to left, accumulating a result. |
| [map](../../cl.ravenhill.keen.mixins/-mappable/map.md) | [common]<br>abstract fun [map](../../cl.ravenhill.keen.mixins/-mappable/map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Mappable](../../cl.ravenhill.keen.mixins/-mappable/index.md)&lt;[T](index.md)&gt;<br>Applies the given transformation function to the value contained within the mappable structure. |
| [take](take.md) | [common]<br>abstract fun [take](take.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;[Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), [Representation](index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;<br>Returns a new representation by keeping the first [n](take.md) elements. |
| [verify](../../cl.ravenhill.keen.mixins/-verifiable/verify.md) | [common]<br>open fun [verify](../../cl.ravenhill.keen.mixins/-verifiable/verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the object. |
