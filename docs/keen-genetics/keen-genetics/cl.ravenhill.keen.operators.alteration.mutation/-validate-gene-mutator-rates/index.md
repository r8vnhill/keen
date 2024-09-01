//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[ValidateGeneMutatorRates](index.md)

# ValidateGeneMutatorRates

class [ValidateGeneMutatorRates](index.md)(individualRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) : [Validator](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-validator/index.md)

Validates the mutation rates for genes, chromosomes, and individuals in a genetic algorithm.

The `ValidateGeneMutatorRates` class serves as a `Validator` for ensuring that the mutation rates provided to a gene mutator are within the valid range of 0, 1. This class is intended to be used through delegation in mutation operators or other components that require validation of mutation rates.

## Usage:

This class is typically used in the context of genetic algorithms where mutation rates must be validated to avoid invalid operations or unexpected behavior. By implementing the `Validator` interface, this class can be easily integrated into other components through delegation.

### Example: Using `ValidateGeneMutatorRates` for Validation

```kotlin
class MyGeneMutator<T, G>(
    individualRate: Double,
    chromosomeRate: Double,
    geneRate: Double
) : Mutator<T, G>, Validator by ValidateGeneMutatorRates(individualRate, chromosomeRate, geneRate)
    where G : Gene<T, G> {
    // Mutator implementation here
}
```

In this example, the `ValidateGeneMutatorRates` class is used to validate the mutation rates for individuals, chromosomes, and genes when constructing a `MyGeneMutator` object. If any of the rates are outside the valid range, an exception is thrown during the initialization of the `MyGeneMutator`.

#### Parameters

common

| | |
|---|---|
| individualRate | The mutation rate for individuals, which must be in the range 0, 1. |
| chromosomeRate | The mutation rate for chromosomes, which must be in the range 0, 1. |
| geneRate | The mutation rate for genes, which must be in the range 0, 1. |

#### Throws

| | |
|---|---|
| CompositeException | if any of the mutation rates fall outside the valid range. |

## Constructors

| | |
|---|---|
| [ValidateGeneMutatorRates](-validate-gene-mutator-rates.md) | [common]<br>constructor(individualRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html))<br>Performs validation of the mutation rates for individuals, chromosomes, and genes. |
