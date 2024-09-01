//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[BooleanChromosome](index.md)/[toString](to-string.md)

# toString

[common]\
open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)

Generates a string representation of the BooleanChromosome object based on the current toStringMode.

The `toString` method returns a string representation of the BooleanChromosome, with two distinct formats depending on the `toStringMode` configured in the [Domain](../../../../keen-core/keen-core/cl.ravenhill.keen/-domain/index.md) object. This flexibility allows for different levels of detail in the output, which can be useful for debugging or displaying concise information.

## Modes:

- 
   **SIMPLE**: The chromosome is represented as a binary string, padded with zeros to align with a chunk size of

1. 
   The binary string is then split into chunks of 4 bits and separated by spaces.

- 
   **DEFAULT**: The chromosome is represented in a more descriptive form, showing the list of genes.

## Example:

Assuming a chromosome with genes corresponding to the values `[true, false, true, true, false]`:

- 
   In `SIMPLE` mode: The output might look like `"0001 0110"`.
- 
   In `DEFAULT` mode: The output might look like `"BooleanChromosome(genes=[True, False, True, True, False])"`.

#### Return

A string representation of the BooleanChromosome object.
