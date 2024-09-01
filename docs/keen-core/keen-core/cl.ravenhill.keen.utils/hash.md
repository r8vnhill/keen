//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[hash](hash.md)

# hash

[common]\
fun [hash](hash.md)(vararg a: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Computes a hash code for a given array of objects.

The `hash` function calculates a hash code by iterating over the provided elements and combining their hash codes. The formula used is `result = PRIME * result + element.hashCode()` for each element, starting with an initial value of 1.

#### Return

The computed hash code as an `Int`.

#### Parameters

common

| | |
|---|---|
| a | Vararg of objects for which the hash code is computed. |
