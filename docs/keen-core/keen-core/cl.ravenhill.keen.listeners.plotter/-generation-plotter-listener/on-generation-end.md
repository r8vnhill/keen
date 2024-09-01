//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.plotter](../index.md)/[GenerationPlotterListener](index.md)/[onGenerationEnd](on-generation-end.md)

# onGenerationEnd

[common]\
open suspend override fun [onGenerationEnd](on-generation-end.md)(state: [S](index.md))

Called at the end of a generation.

This method records the offspring from the population into the `currentGeneration` record, capturing the outcomes of the generation for later analysis or visualization.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state, containing the population and other relevant data. |
