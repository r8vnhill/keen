//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AbstractEvolver](index.md)/[evolve](evolve.md)

# evolve

[common]\
open suspend override fun [evolve](evolve.md)(): [S](index.md)

Executes the evolutionary process.

This method manages the main loop of the evolutionary algorithm, invoking lifecycle listeners at the start and end of the evolution, as well as at the start and end of each generation. The process continues until one of the configured limits is met, at which point the final state is returned.

#### Return

The final evolutionary state after the process is complete.
