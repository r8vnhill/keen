//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[AlterationListener](index.md)/[onAlterationStart](on-alteration-start.md)

# onAlterationStart

[common]\
open fun [onAlterationStart](on-alteration-start.md)(state: [S](index.md))

Called at the start of the alteration phase in the evolutionary process.

This method is invoked when the alteration phase begins, allowing you to perform any necessary setup or logging before the genetic operations are applied to the population. By default, this method does nothing (`Unit`), but it can be overridden to perform custom actions.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state, providing context for the alteration phase. |
