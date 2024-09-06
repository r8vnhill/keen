//[keen-core](../../index.md)/[cl.ravenhill.keen.repr](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Feature](-feature/index.md) | [common]<br>interface [Feature](-feature/index.md)&lt;[T](-feature/index.md), [F](-feature/index.md) : [Feature](-feature/index.md)&lt;[T](-feature/index.md), [F](-feature/index.md)&gt;&gt; : [Verifiable](../cl.ravenhill.keen.mixins/-verifiable/index.md), [Mappable](../cl.ravenhill.keen.mixins/-mappable/index.md)&lt;[T](-feature/index.md)&gt; <br>Represents a fundamental component in an evolutionary algorithm. |
| [Representation](-representation/index.md) | [common]<br>interface [Representation](-representation/index.md)&lt;[T](-representation/index.md), [F](-representation/index.md) : [Feature](-feature/index.md)&lt;[T](-representation/index.md), [F](-representation/index.md)&gt;&gt; : [Verifiable](../cl.ravenhill.keen.mixins/-verifiable/index.md), [FlatMappable](../cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](-representation/index.md)&gt; , [Foldable](../cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](-representation/index.md)&gt; , [Mappable](../cl.ravenhill.keen.mixins/-mappable/index.md)&lt;[T](-representation/index.md)&gt; <br>Represents a generic structure for individuals in an evolutionary algorithm. |
| [RepresentationFactory](-representation-factory/index.md) | [common]<br>interface [RepresentationFactory](-representation-factory/index.md)&lt;[T](-representation-factory/index.md), [F](-representation-factory/index.md) : [Feature](-feature/index.md)&lt;[T](-representation-factory/index.md), [F](-representation-factory/index.md)&gt;, [R](-representation-factory/index.md) : [Representation](-representation/index.md)&lt;[T](-representation-factory/index.md), [F](-representation-factory/index.md)&gt;&gt;<br>Factory interface for creating representations in an evolutionary algorithm. |
