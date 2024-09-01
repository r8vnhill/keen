//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[BooleanChromosomeFactory](index.md)/[trueRate](true-rate.md)

# trueRate

[common]\
var [trueRate](true-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)

The probability that a gene in the chromosome will be `True`.

This property allows customization of the ratio between `True` and `False` genes within the generated chromosome. By adjusting the `trueRate`, you can control how likely it is for a gene to be set to `True` during the chromosome generation process. The value must be between 0.0 and 1.0, inclusive.
