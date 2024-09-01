//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.printer](../index.md)/[GenerationPrinterListener](index.md)/[onGenerationEnd](on-generation-end.md)

# onGenerationEnd

[common]\
open suspend override fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))

Called at the end of each generation.

This method records the duration of the generation, updates the evolution record with the current population and its offspring, and calculates the number of steady generations.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state at the end of the generation. |
