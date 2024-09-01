//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[sequence](sequence.md)

# sequence

[common]\
fun &lt;[L](sequence.md), [R](sequence.md)&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;Either&lt;[L](sequence.md), [R](sequence.md)&gt;&gt;.[sequence](sequence.md)(): Either&lt;[L](sequence.md), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](sequence.md)&gt;&gt;

Sequences a list of `Either` values, transforming a `List<Either<L, R>>` into an `Either<L, List<R>>`.

The `sequence` function processes a list of Either values and attempts to collect all the `Right` values into a single Either.Right containing a list of the [R](sequence.md) values. If any `Either` in the list is `Left`, the function short-circuits and returns the first `Left` encountered, preserving the error.

## Example Usage:

```kotlin
val results: List<Either<Error, Int>> = listOf(1.right(), 2.right(), 3.right())
val sequenced: Either<Error, List<Int>> = results.sequence()

when (sequenced) {
    is Either.Right -> println("All values are successfully collected: ${sequenced.value}")
    is Either.Left -> println("An error occurred: ${sequenced.value}")
}
```

#### Return

An `Either` where the `Right` side contains a list of `R` values if all computations succeeded, or a `Left` value representing the first encountered error.

#### Parameters

common

| | |
|---|---|
| L | The type of the `Left` value, representing an error or failure. |
| R | The type of the `Right` value, representing a success. |
