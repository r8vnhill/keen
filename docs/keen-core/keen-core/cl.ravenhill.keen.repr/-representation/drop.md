//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Representation](index.md)/[drop](drop.md)

# drop

[common]\
abstract fun [drop](drop.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;[Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), [Representation](index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;

Returns a new representation by removing the first [n](drop.md) elements.

The `drop` method allows removing a certain number of elements from the representation, starting from the beginning. It produces a new representation with [n](drop.md) elements removed. If [n](drop.md) exceeds the size of the representation, an exception is returned.

#### Return

An Either containing the new representation if successful, or an exception if [n](drop.md) exceeds the size.

#### Parameters

common

| | |
|---|---|
| n | The number of elements to drop from the beginning. |
