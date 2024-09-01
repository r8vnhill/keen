//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[GenerationRecord](index.md)/[steady](steady.md)

# steady

[common]\
var [steady](steady.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

The counter for steady generations. This tracks how many consecutive generations have remained steady, meaning no significant changes in fitness values.  It must not be negative.

#### Throws

| | |
|---|---|
| CompositeException | if the counter is negative. |
