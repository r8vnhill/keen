//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)/[get](get.md)

# get

[common]\
open operator fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [G](index.md)&gt;

Retrieves the gene at the specified index within the chromosome.

The `get` operator function provides safe access to the genes within a chromosome by validating the provided index before attempting to retrieve the gene. If the index is within bounds, the function returns the gene wrapped in an Either.Right. If the index is out of bounds, the function returns an Either.Left containing a CompositeException that describes the error.

#### Return

An `Either<CompositeException, G>` where [G](index.md) is the type of the gene:

- 
   `Either.Right<G>` containing the gene if the index is valid.
- 
   `Either.Left<CompositeException>` containing an error if the index is invalid.

#### Parameters

common

| | |
|---|---|
| index | The index of the gene to be retrieved from the chromosome. |
