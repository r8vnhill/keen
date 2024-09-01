//[keen-core](../../../index.md)/[cl.ravenhill.keen.limits](../index.md)/[Limit](index.md)/[invoke](invoke.md)

# invoke

[common]\
operator fun [invoke](invoke.md)(state: [S](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Evaluates the limit condition on the given state.

This operator function applies the predicate function to the current state of the evolutionary process, determining whether the limit has been reached.

#### Return

`true` if the limit condition is met, `false` otherwise.

#### Parameters

common

| | |
|---|---|
| state | The current state of the evolutionary process. |
