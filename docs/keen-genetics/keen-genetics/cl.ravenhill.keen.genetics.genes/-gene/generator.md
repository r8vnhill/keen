//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genes](../index.md)/[Gene](index.md)/[generator](generator.md)

# generator

[common]\
abstract val [generator](generator.md): ([Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html)) -&gt; [T](index.md)

A function that generates new values for the gene.

The `generator` function is responsible for producing new values that the gene can hold. This function is typically used during mutation to introduce variability into the gene's value.

### Example:

```kotlin
override val generator: (Random) -> Int = { random -> random.nextInt(0, 100) }
```
