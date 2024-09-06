//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Representation](index.md)/[take](take.md)

# take

[common]\
abstract fun [take](take.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;[Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), [Representation](index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;

Returns a new representation by keeping the first [n](take.md) elements.

The `take` method allows extracting the first [n](take.md) elements from the representation, returning a new representation containing only these elements. If [n](take.md) exceeds the size of the representation, an exception is returned.

#### Return

An Either containing the new representation if successful, or an exception if [n](take.md) exceeds the size.

#### Parameters

common

| | |
|---|---|
| n | The number of elements to take from the beginning. |
