//[keen-genetics](../../../../index.md)/[cl.ravenhill.keen.genetics.genes](../../index.md)/[BooleanGene](../index.md)/[True](index.md)

# True

[common]\
data object [True](index.md) : [BooleanGene](../index.md)

Represents the `True` state of the boolean gene.

The `True` object holds the value `true` and is one of the two possible states of a [BooleanGene](../index.md).

## Properties

| Name | Summary |
|---|---|
| [generator](../generator.md) | [common]<br>open override val [generator](../generator.md): ([Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>A function that generates new boolean values for the gene, typically used in mutation operations. |
| [value](value.md) | [common]<br>open override val [value](value.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true |

## Functions

| Name | Summary |
|---|---|
| [copyWithValue](../copy-with-value.md) | [common]<br>open override fun [copyWithValue](../copy-with-value.md)(value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BooleanGene](../index.md)<br>Duplicates the gene with a new specified boolean value. |
| [flatMap](../-false/index.md#600781797%2FFunctions%2F-1476930196) | [common]<br>open fun [flatMap](../-false/index.md#600781797%2FFunctions%2F-1476930196)(f: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [BooleanGene](../index.md)): [BooleanGene](../index.md) |
| [map](../-false/index.md#94155127%2FFunctions%2F-1476930196) | [common]<br>open override fun [map](../-false/index.md#94155127%2FFunctions%2F-1476930196)(transform: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BooleanGene](../index.md) |
| [mutate](../../-gene/mutate.md) | [common]<br>open fun [mutate](../../-gene/mutate.md)(random: [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html) = Domain.random): [BooleanGene](../index.md)<br>Mutates the gene by generating a new value using the `generator` function. |
| [toInt](../to-int.md) | [common]<br>open fun [toInt](../to-int.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Converts the boolean gene's value to an integer. |
| [toList](../../-gene/index.md#-321272454%2FFunctions%2F-1476930196) | [common]<br>open fun [toList](../../-gene/index.md#-321272454%2FFunctions%2F-1476930196)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [verify](../../-gene/index.md#629456716%2FFunctions%2F-1476930196) | [common]<br>open fun [verify](../../-gene/index.md#629456716%2FFunctions%2F-1476930196)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
