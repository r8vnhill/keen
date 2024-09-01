//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[FlatMappable](index.md)

# FlatMappable

interface [FlatMappable](index.md)&lt;out [T](index.md)&gt;

Represents an entity that can be flattened and mapped over.

The `FlatMappable` interface defines the structure for an entity that supports flattening and flat-mapping operations. This is particularly useful in evolutionary algorithms and other contexts where hierarchical or nested structures need to be processed.

## Usage:

Implement this interface in classes where flattening and flat-mapping functionality is required. The `flatten` method should be implemented to return a list of elements, and the `flatMap` method provides a default implementation that applies a transformation function to each flattened element.

### Example:

```kotlin
class TreeNode<T>(val value: T, val children: List<TreeNode<T>>) : FlatMappable<T> {
    override fun flatten() = listOf(value) + children.flatMap { it.flatten() }
}

val tree = TreeNode(1, listOf(TreeNode(2, emptyList()), TreeNode(3, emptyList())))
val flattened = tree.flatten() // [1, 2, 3]
val flatMapped = tree.flatMap { listOf(it, it * 10) } // [1, 10, 2, 20, 3, 30]
```

#### Parameters

common

| | |
|---|---|
| T | The type of elements in the flattenable structure. |

#### Inheritors

| |
|---|
| [Individual](../../cl.ravenhill.keen/-individual/index.md) |
| [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md) |
| [Representation](../../cl.ravenhill.keen.repr/-representation/index.md) |

## Functions

| Name | Summary |
|---|---|
| [flatMap](flat-map.md) | [common]<br>open fun &lt;[R](flat-map.md)&gt; [flatMap](flat-map.md)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](flat-map.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](flat-map.md)&gt;<br>Applies a transformation function to each flattened element and returns a list of the results. |
| [flatten](flatten.md) | [common]<br>abstract fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the structure into a list of elements. |
