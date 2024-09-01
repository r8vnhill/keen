//[keen-core](../../index.md)/[cl.ravenhill.keen.listeners.records](index.md)/[applyToGeneration](apply-to-generation.md)

# applyToGeneration

[common]\
fun &lt;[T](apply-to-generation.md), [F](apply-to-generation.md) : [Feature](../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](apply-to-generation.md), [F](apply-to-generation.md)&gt;, [R](apply-to-generation.md) : [Representation](../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](apply-to-generation.md), [F](apply-to-generation.md)&gt;&gt; [applyToGeneration](apply-to-generation.md)(generation: [Box](../cl.ravenhill.keen.utils.box/-box/index.md)&lt;[GenerationRecord](-generation-record/index.md)&lt;[T](apply-to-generation.md), [F](apply-to-generation.md), [R](apply-to-generation.md)&gt;?&gt;, block: [GenerationRecord](-generation-record/index.md)&lt;[T](apply-to-generation.md), [F](apply-to-generation.md), [R](apply-to-generation.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Applies a transformation to a [GenerationRecord](-generation-record/index.md) contained within a [Box](../cl.ravenhill.keen.utils.box/-box/index.md).

The `mapGeneration` function allows you to apply a transformation or operation to a `GenerationRecord` within a nullable `Box`. If the `Box` contains a `GenerationRecord`, the provided [block](apply-to-generation.md) of operations will be applied to it. If the `Box` is null, the function does nothing.

## Usage:

This function is useful when you need to perform an operation on a `GenerationRecord` that is wrapped in a `Box` and may be null. The function safely handles the nullability and only applies the `block` if the `GenerationRecord` is present. This helps to prevent potential null pointer exceptions and simplifies the code that operates on the `GenerationRecord`.

#### Parameters

common

| | |
|---|---|
| generation | The `Box` containing a `GenerationRecord` or null. |
| block | The block of code to be applied to the `GenerationRecord` if it exists. This block operates on the `GenerationRecord` and can modify its properties. |
| T | The type of value held by the features. |
| F | The type of feature, which must extend [Feature](../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../cl.ravenhill.keen.repr/-representation/index.md). |
