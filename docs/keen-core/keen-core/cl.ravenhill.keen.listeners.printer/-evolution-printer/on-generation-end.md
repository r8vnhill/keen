//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.printer](../index.md)/[EvolutionPrinter](index.md)/[onGenerationEnd](on-generation-end.md)

# onGenerationEnd

[common]\
open suspend override fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))

Called at the end of each generation.

This method delegates the generation end processing to the `GenerationPrinterListener` and checks if the current generation is a multiple of `every`. If so, it prints the evolution details.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state at the end of the generation. |
