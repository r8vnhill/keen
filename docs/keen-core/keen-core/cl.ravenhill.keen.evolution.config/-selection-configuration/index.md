//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.config](../index.md)/[SelectionConfiguration](index.md)

# SelectionConfiguration

[common]\
data class [SelectionConfiguration](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val survivalRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), val parentSelector: [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, val survivorSelector: [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;)

## Constructors

| | |
|---|---|
| [SelectionConfiguration](-selection-configuration.md) | [common]<br>constructor(survivalRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), parentSelector: [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, survivorSelector: [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [parentSelector](parent-selector.md) | [common]<br>val [parentSelector](parent-selector.md): [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; |
| [survivalRate](survival-rate.md) | [common]<br>val [survivalRate](survival-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [survivorSelector](survivor-selector.md) | [common]<br>val [survivorSelector](survivor-selector.md): [Selector](../../cl.ravenhill.keen.operators.selection/-selector/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; |
