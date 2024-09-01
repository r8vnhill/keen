//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[Listener](index.md)/[copy](copy.md)

# copy

[common]\
abstract fun [copy](copy.md)(): [Listener](index.md)

Creates and returns a copy of the listener.

This method is intended to return a copy of the listener, which can be used in contexts where the listener needs to be duplicated without modifying the original instance. The implementation of this method should ensure that the copied listener is independent of the original.

#### Return

A new instance of the listener or a deep copy, depending on the implementation.
