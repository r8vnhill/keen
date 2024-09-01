//[keen-core](../../index.md)/[cl.ravenhill.keen.utils.box](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Box](-box/index.md) | [common]<br>sealed interface [Box](-box/index.md)&lt;[T](-box/index.md)&gt;<br>A sealed interface representing a container for a value. |
| [ImmutableBox](-immutable-box/index.md) | [common]<br>data class [ImmutableBox](-immutable-box/index.md)&lt;[T](-immutable-box/index.md)&gt;(val value: [T](-immutable-box/index.md)?) : [Box](-box/index.md)&lt;[T](-immutable-box/index.md)&gt; <br>An immutable implementation of the `Box` interface. |
| [MutableBox](-mutable-box/index.md) | [common]<br>data class [MutableBox](-mutable-box/index.md)&lt;[T](-mutable-box/index.md)&gt;(var value: [T](-mutable-box/index.md)?) : [Box](-box/index.md)&lt;[T](-mutable-box/index.md)&gt; <br>A mutable implementation of the `Box` interface. |
