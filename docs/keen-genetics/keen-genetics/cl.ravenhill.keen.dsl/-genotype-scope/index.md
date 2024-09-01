//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.dsl](../index.md)/[GenotypeScope](index.md)

# GenotypeScope

[common]\
class [GenotypeScope](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

## Constructors

| | |
|---|---|
| [GenotypeScope](-genotype-scope.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [chromosomes](chromosomes.md) | [common]<br>val [chromosomes](chromosomes.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ChromosomeFactory](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [chromosomeOf](../chromosome-of.md) | [common]<br>fun &lt;[T](../chromosome-of.md), [G](../chromosome-of.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](../chromosome-of.md), [G](../chromosome-of.md)&gt;&gt; [GenotypeScope](index.md)&lt;[T](../chromosome-of.md), [G](../chromosome-of.md)&gt;.[chromosomeOf](../chromosome-of.md)(lazyFactory: [ChromosomeScope](../-chromosome-scope/index.md)&lt;[T](../chromosome-of.md)&gt;.() -&gt; [ChromosomeFactory](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome-factory/index.md)&lt;[T](../chromosome-of.md), [G](../chromosome-of.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
