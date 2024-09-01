//[keen-genetics](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[roundUpToMultipleOf](round-up-to-multiple-of.md)

# roundUpToMultipleOf

[common]\
infix fun [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html).[roundUpToMultipleOf](round-up-to-multiple-of.md)(i: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Rounds up the current integer to the nearest multiple of a specified integer.

This function takes an integer value and rounds it up to the nearest multiple of a specified integer [i](round-up-to-multiple-of.md). If the current integer is already a multiple of [i](round-up-to-multiple-of.md), it returns the current integer. If [i](round-up-to-multiple-of.md) is zero, the function simply returns the current integer. Otherwise, it calculates the next multiple of [i](round-up-to-multiple-of.md) that is greater than or equal to the current integer.

## Usage:

```kotlin
val result1 = 7 roundUpToMultipleOf 5 // Returns 10
val result2 = 12 roundUpToMultipleOf 0 // Returns 12
val result3 = 15 roundUpToMultipleOf 5 // Returns 15
```

In these examples:

- 
   `result1` is 10 because the next multiple of 5 greater than 7 is 10.
- 
   `result2` is 12 because when the specified multiple is 0, the function returns the current integer.
- 
   `result3` is 15 because 15 is already a multiple of 5.

#### Return

The smallest integer that is greater than or equal to the current integer and is a multiple of `i`.

#### Parameters

common

| | |
|---|---|
| i | The integer value to which the current integer is to be rounded up. |
