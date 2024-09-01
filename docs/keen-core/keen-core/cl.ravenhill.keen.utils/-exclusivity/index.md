//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils](../index.md)/[Exclusivity](index.md)

# Exclusivity

[common]\
enum [Exclusivity](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Exclusivity](index.md)&gt; 

Enum representing the exclusivity policy in genetic operations such as crossover in evolutionary algorithms.

The `Exclusivity` enum defines two distinct exclusivity modes that govern how parent genes are selected and combined during crossover operations in an evolutionary algorithm. These modes determine whether the same parent genes can be repeatedly selected across multiple crossover operations, especially with different partners, or if each gene selection must come from a unique parent within a single crossover operation.

## Entries

| | |
|---|---|
| [NON_EXCLUSIVE](-n-o-n_-e-x-c-l-u-s-i-v-e/index.md) | [common]<br>[NON_EXCLUSIVE](-n-o-n_-e-x-c-l-u-s-i-v-e/index.md)<br>Non-exclusive mode allows the same parent gene to be selected multiple times across different crossover operations, even with different partners. |
| [EXCLUSIVE](-e-x-c-l-u-s-i-v-e/index.md) | [common]<br>[EXCLUSIVE](-e-x-c-l-u-s-i-v-e/index.md)<br>Exclusive mode ensures that each gene comes from a different parent during a single crossover operation, enhancing genetic diversity by preventing over-representation of certain genes across multiple crossovers with different partners. |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.enums/-enum-entries/index.html)&lt;[Exclusivity](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](../-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-372974862%2FProperties%2F1902964177) | [common]<br>val [name](../-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-372974862%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-739389684%2FProperties%2F1902964177) | [common]<br>val [ordinal](../-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-739389684%2FProperties%2F1902964177): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Exclusivity](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Exclusivity](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
