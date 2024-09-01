//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.executors.evaluation](../index.md)/[ForceEvaluation](index.md)

# ForceEvaluation

enum [ForceEvaluation](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ForceEvaluation](index.md)&gt; 

Enum representing the evaluation strategy in an evolutionary algorithm.

The `ForceEvaluation` enum defines different strategies for evaluating individuals within a population in the context of an evolutionary algorithm. This is particularly useful when deciding which individuals need to be evaluated based on their current state, whether they are new, have changed, or whether a full re-evaluation of all individuals is required.

## Evaluation Strategies:

- 
   **ALL**: Forces the evaluation of all individuals in the population, regardless of whether they have been evaluated before. This strategy ensures that every individual is reassessed, which can be useful when there are changes in the evaluation criteria or when complete consistency is required across the population.
- 
   **NEW**: Evaluates only new individuals or those that have changed since the last evaluation. This strategy is efficient when most of the population remains unchanged and only a subset of individuals needs to be evaluated.
- 
   **NONE**: Skips the evaluation process entirely. This can be useful in scenarios where evaluation is optional or has already been performed by another process.

#### See also

| |
|---|
| [EvaluationExecutor](../-evaluation-executor/index.md) |

## Entries

| | |
|---|---|
| [ALL](-a-l-l/index.md) | [common]<br>[ALL](-a-l-l/index.md)<br>Forces the evaluation of all individuals in the population, regardless of their current evaluation status. |
| [NEW](-n-e-w/index.md) | [common]<br>[NEW](-n-e-w/index.md)<br>Evaluates only new or changed individuals since the last evaluation. |
| [NONE](-n-o-n-e/index.md) | [common]<br>[NONE](-n-o-n-e/index.md)<br>Skips the evaluation process entirely. |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.enums/-enum-entries/index.html)&lt;[ForceEvaluation](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-372974862%2FProperties%2F1902964177) | [common]<br>val [name](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-372974862%2FProperties%2F1902964177): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-739389684%2FProperties%2F1902964177) | [common]<br>val [ordinal](../../cl.ravenhill.keen.utils/-sorting-strategy/-u-n-s-o-r-t-e-d/index.md#-739389684%2FProperties%2F1902964177): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ForceEvaluation](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ForceEvaluation](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
