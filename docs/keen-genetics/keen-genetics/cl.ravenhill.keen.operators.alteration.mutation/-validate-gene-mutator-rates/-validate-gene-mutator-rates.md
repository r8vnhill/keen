//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.operators.alteration.mutation](../index.md)/[ValidateGeneMutatorRates](index.md)/[ValidateGeneMutatorRates](-validate-gene-mutator-rates.md)

# ValidateGeneMutatorRates

[common]\
constructor(individualRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), chromosomeRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), geneRate: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html))

Performs validation of the mutation rates for individuals, chromosomes, and genes.

#### Parameters

common

| | |
|---|---|
| individualRate | The mutation rate for individuals, which must be in the range 0, 1. |
| chromosomeRate | The mutation rate for chromosomes, which must be in the range 0, 1. |
| geneRate | The mutation rate for genes, which must be in the range 0, 1. |
