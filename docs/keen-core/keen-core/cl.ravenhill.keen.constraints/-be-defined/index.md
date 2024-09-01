//[keen-core](../../../index.md)/[cl.ravenhill.keen.constraints](../index.md)/[BeDefined](index.md)

# BeDefined

data object [BeDefined](index.md) : IntConstraint

Represents a constraint that ensures an `Int` value has been initialized.

This constraint is particularly useful for checking if an `Int` property initialized with [Delegates.notNull](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-delegates/not-null.html) has been set. It attempts to access the value and returns `true` if the value is accessible, or `false` if it throws an `IllegalStateException`, indicating that the value has not been initialized.

#### Throws

| | |
|---|---|
| [IllegalStateException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-state-exception/index.html) | if the value has not been initialized. |

## Properties

| Name | Summary |
|---|---|
| [validator](validator.md) | [common]<br>open override val [validator](validator.md): ([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>The validation function that checks if the `Int` value has been initialized. |

## Functions

| Name | Summary |
|---|---|
| [generateException](index.md#1157509239%2FFunctions%2F1902964177) | [common]<br>open override fun [generateException](index.md#1157509239%2FFunctions%2F1902964177)(description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): IntConstraintException |
