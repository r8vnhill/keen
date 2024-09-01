//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.mixins](../index.md)/[AlterationListener](index.md)/[onAlterationEnd](on-alteration-end.md)

# onAlterationEnd

[common]\
open fun [onAlterationEnd](on-alteration-end.md)(state: [S](index.md))

Called at the end of the alteration phase in the evolutionary process.

This method is invoked when the alteration phase ends, allowing you to perform any necessary teardown or logging after the genetic operations have been applied to the population. By default, this method does nothing (`Unit`), but it can be overridden to perform custom actions.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state, providing context for the alteration phase. |
