//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[nextChar](next-char.md)

# nextChar

[common]\
fun [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html).[nextChar](next-char.md)(range: [ClosedRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-closed-range/index.html)&lt;[Char](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)&gt; = Char.MIN_VALUE..Char.MAX_VALUE, filter: ([Char](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = { true }): [Char](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char/index.html)

Generates a random `Char` within a specified range, with an optional filtering condition.

This extension function for the `Random` class provides a way to generate a random character within a given range. An optional filter function can be provided to exclude certain characters from the selection. The function repeatedly generates random characters within the range until one satisfies the filter condition.

## Example:

```kotlin
val randomChar = Random.nextChar('a'..'z') // Random lowercase letter
val digit = Random.nextChar() { it.isDigit() } // Random digit
```

In the first example, `randomChar` will be a random lowercase letter. In the second example, `digit` will be a random digit, although the range already ensures that.

#### Return

A randomly generated character that falls within the specified range and satisfies the filter condition.

#### Parameters

common

| | |
|---|---|
| range | The range of characters to select from. Defaults to the full range of `Char` from `Char.MIN_VALUE` to `Char.MAX_VALUE`. |
| filter | An optional lambda function that returns `true` for acceptable characters and `false` for those that should be excluded. Defaults to a lambda that accepts all characters. |
