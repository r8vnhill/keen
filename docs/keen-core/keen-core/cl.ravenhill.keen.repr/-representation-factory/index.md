//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[RepresentationFactory](index.md)

# RepresentationFactory

interface [RepresentationFactory](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;

Factory interface for creating representations in an evolutionary algorithm.

The `RepresentationFactory` interface defines a contract for factories that generate representations, which are higher-level abstractions of collections of features (such as genes in a genetic algorithm). This interface is designed to be flexible and supports asynchronous operations through Kotlin's `suspend` functions, making it suitable for environments where non-blocking operations are essential, such as Kotlin/JS.

## Usage:

This interface is intended to be implemented by factories that create specific types of representations within an evolutionary algorithm. The factory provides a mechanism for generating representations of a predefined size, using a random generator to introduce variability. The `invoke` function is marked as `suspend` to enable asynchronous execution, facilitating efficient, non-blocking construction of representations.

### Example: Implementing a Custom Representation Factory

```kotlin
class MyRepresentationFactory : RepresentationFactory<Int, IntGene, IntChromosome> {
    override var size: Int = 10

    override suspend fun invoke(random: Random): Result<IntChromosome> = runCatching {
        val genes = List(size) { IntGene(random.nextInt(0, 100)) }
        IntChromosome(genes)
    }
}
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features within the representation. |
| F | The type of the feature, which must extend [Feature](../-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../-representation/index.md). |

## Properties

| Name | Summary |
|---|---|
| [size](size.md) | [common]<br>abstract var [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of features to include in the generated representation. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>abstract suspend operator fun [invoke](invoke.md)(): Either&lt;[InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [R](index.md)&gt;<br>Asynchronously creates a representation of the predefined size. |
