//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[ToStringMode](index.md)

# ToStringMode

[common]\
enum [ToStringMode](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ToStringMode](index.md)&gt; 

Enum representing the different modes for converting objects to their string representation.

The `ToStringMode` enum provides two modes for converting objects to strings: `SIMPLE` and `DEFAULT`. These modes can be used to control the level of detail included in the string representation.

## Usage:

This enum can be used to specify the desired mode when converting objects to strings, allowing for either a simple or a detailed representation.

### Example:

```kotlin
val mode = ToStringMode.SIMPLE
val detailedMode = ToStringMode.DEFAULT
```

## Entries

| | |
|---|---|
| [SIMPLE](-s-i-m-p-l-e/index.md) | [common]<br>[SIMPLE](-s-i-m-p-l-e/index.md) |
| [DEFAULT](-d-e-f-a-u-l-t/index.md) | [common]<br>[DEFAULT](-d-e-f-a-u-l-t/index.md) |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.enums/-enum-entries/index.html)&lt;[ToStringMode](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-372974862%2FProperties%2F1902964177) | [common]<br>val [name](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-372974862%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-739389684%2FProperties%2F1902964177) | [common]<br>val [ordinal](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-739389684%2FProperties%2F1902964177): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ToStringMode](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ToStringMode](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
