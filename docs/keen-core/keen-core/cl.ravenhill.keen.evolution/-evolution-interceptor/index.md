//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution](../index.md)/[EvolutionInterceptor](index.md)

# EvolutionInterceptor

class [EvolutionInterceptor](index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](index.md)&gt;(val before: ([S](index.md)) -&gt; [S](index.md), val after: ([S](index.md)) -&gt; [S](index.md))

An interceptor for evolutionary processes that allows customization of actions to be performed before and after each evolutionary step.

The `EvolutionInterceptor` class enables you to insert custom logic before and after the evolutionary process progresses. This can be useful for tasks such as logging, modifying the evolutionary state, or injecting additional behaviors at specific stages.

## Usage:

You can use this class to define actions that should occur before and after the evolutionary algorithm advances to a new state. The `before` function is applied to the state before the evolutionary step, and the `after` function is applied after the step.

### Example:

Implementing an interceptor that logs the state before and after each evolutionary step:

```kotlin
val loggingInterceptor = EvolutionInterceptor<MyType, MyFeature, MyRepresentation, MyState>(
    before = { state ->
        println("Before evolution: $state")
        state
    },
    after = { state ->
        println("After evolution: $state")
        state
    }
)
```

### Identity Interceptor:

The `identity` interceptor does nothing and simply returns the state unchanged.

```kotlin
val identityInterceptor = EvolutionInterceptor.identity<MyType, MyFeature, MyRepresentation, MyState>()
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features in the representation. |
| F | The type of the feature, which must extend Feature. |
| R | The type of the representation, which must extend Representation. |
| S | The type of the evolutionary state. |

## Constructors

| | |
|---|---|
| [EvolutionInterceptor](-evolution-interceptor.md) | [common]<br>constructor(before: ([S](index.md)) -&gt; [S](index.md), after: ([S](index.md)) -&gt; [S](index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [after](after.md) | [common]<br>val [after](after.md): ([S](index.md)) -&gt; [S](index.md)<br>A function applied to the state after the evolutionary step. |
| [before](before.md) | [common]<br>val [before](before.md): ([S](index.md)) -&gt; [S](index.md)<br>A function applied to the state before the evolutionary step. |
