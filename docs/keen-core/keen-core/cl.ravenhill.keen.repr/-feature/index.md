//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Feature](index.md)

# Feature

interface [Feature](index.md)&lt;[T](index.md), [F](index.md) : [Feature](index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [Verifiable](../../cl.ravenhill.keen.mixins/-verifiable/index.md)

Represents a fundamental component in an evolutionary algorithm.

The `Feature` interface defines the basic structure and behavior of a feature, which is an atomic unit within an evolutionary algorithm. A feature typically represents a single element in a genetic structure, such as a gene in a chromosome. This interface provides core functionality for duplicating a feature with a new value and binding transformations to the feature's value.

## Usage:

Implement this interface to create concrete classes that represent specific types of features in evolutionary algorithms. The `bind` method facilitates chaining operations on the feature's value, enabling complex transformations and computations.

### Example 1: Implementing a Simple Gene Feature

```kotlin
data class IntGene(override val value: Int) : Feature<Int, IntGene> {
    override fun copyWithValue(value: Int) = IntGene(value)
}
```

### Example 2: Binding a Transformation

```kotlin
val gene = IntGene(10)
val transformedGene = gene.bind { IntGene(it * 2) }
println(transformedGene.value) // Output: 20
```

## Recommendation for Functional Programmers:

If you are familiar with functional programming and the concept of monads, you can implement subclasses of `Feature` as monads. This involves ensuring that your subclass respects the monad laws (left identity, right identity, and associativity) and providing a `pure` (or `unit`) function to wrap values into your monadic structure.

### How to Implement Subclasses as Monads:

1. 
   **Define a** `pure` **Function**: This function should take a raw value of type `T` and return an instance of your     subclass. This allows you to wrap values in the monadic context.
   
   ```kotlin
    companion object {
        fun pure(value: Int): IntGene = IntGene(value)
    }
   ```
2. 
   **Respect the Monad Laws**: Ensure that your subclass's implementation of `bind` respects the monad laws:
3. - 
      **Left Identity**: `pure(a).flatMap(f)` should be equivalent to `f(a)`.
   - 
      **Right Identity**: `m.flatMap(::pure)` should be equivalent to `m`.
   - 
      **Associativity**: `(m.flatMap(f)).flatMap(g)` should be equivalent to `m.flatMap { x -> f(x).flatMap(g) }`.

### Benefits of Implementing Subclasses as Monads:

- 
   **Composability**: Monads allow you to chain operations in a clean and consistent way, enabling the composition     of complex behaviors from simple functions.
- 
   **Error Handling**: Monads provide a structured way to handle errors, missing values, or other computational     contexts (e.g., `Option`, `Either`).
- 
   **Consistency**: By adhering to the monad laws, you ensure that your code behaves predictably and consistently,     making it easier to reason about.

## Recommendation to Use Data Classes for Subclasses:

It is recommended to implement subclasses of `Feature` as data classes. Kotlin data classes provide several benefits that align well with the goals of functional programming and evolutionary algorithms:

1. 
   **Automatic Implementations**: Data classes automatically provide implementations for `equals()`, `hashCode()`,     `toString()`, and `copy()`. This reduces boilerplate and ensures that your feature instances are easily comparable     and printable.
2. 
   **Immutability**: Data classes are typically used with immutable properties, which is a key principle in     functional programming. Immutability ensures that once a feature is created, it cannot be modified, leading to     more predictable and safer code.
3. 
   **Ease of Use**: The `copy()` method provided by data classes makes it easy to create new instances with modified     values, which is particularly useful when implementing the `copyWithValue` method.

### Example of a Data Class Implementation:

```kotlin
data class IntGene(override val value: Int) : Feature<Int, IntGene> {
    override fun copyWithValue(value: Int) = copy(value = value)

    companion object {
        fun pure(value: Int): IntGene = IntGene(value)
    }
}
```

### Benefits of Using Data Classes:

- 
   **Reduced Boilerplate**: Data classes eliminate much of the repetitive code required for implementing common functions, making your code cleaner and more maintainable.
- 
   **Immutability and Safety**: By leveraging immutability, data classes help you avoid common bugs related to unintended mutations, leading to safer and more predictable code.
- 
   **Convenient Copying**: The `copy()` method simplifies the creation of modified instances, which is a frequent requirement in evolutionary algorithms.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the feature. |
| F | The type of the feature itself, which must extend [Feature](index.md). |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>abstract val [value](value.md): [T](index.md)<br>The value held by the feature. |

## Functions

| Name | Summary |
|---|---|
| [copyWithValue](copy-with-value.md) | [common]<br>abstract fun [copyWithValue](copy-with-value.md)(value: [T](index.md)): [F](index.md)<br>Creates a duplicate of the feature with a new specified value. |
| [flatMap](flat-map.md) | [common]<br>open fun [flatMap](flat-map.md)(f: ([T](index.md)) -&gt; [F](index.md)): [F](index.md)<br>Applies a function to the feature's value and returns a new feature instance with the transformed value. |
| [toList](to-list.md) | [common]<br>open fun [toList](to-list.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Converts the feature's value into a list representation. |
| [verify](../../cl.ravenhill.keen.mixins/-verifiable/verify.md) | [common]<br>open fun [verify](../../cl.ravenhill.keen.mixins/-verifiable/verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the object. |
