//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[RepresentationFactory](index.md)/[invoke](invoke.md)

# invoke

[common]\
abstract suspend operator fun [invoke](invoke.md)(): Either&lt;[InitializationException](../../cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [R](index.md)&gt;

Asynchronously creates a representation of the predefined size.

The `invoke` function is the primary method for generating representations. It is a `suspend` function, allowing for non-blocking execution, which is particularly useful in environments like Kotlin/JS or when dealing with large-scale, computationally intensive tasks.

#### Return

A [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html) containing the generated representation, or an exception if the generation fails.
