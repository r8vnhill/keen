//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.alteration](../index.md)/[Alterer](index.md)

# Alterer

interface [Alterer](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt; : [Operator](../../cl.ravenhill.keen.operators/-operator/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; 

Marker interface for operators that alter the genetic representation in an evolutionary algorithm.

The `Alterer` interface serves as a marker for operators that are specifically designed to modify or alter the genetic representation of individuals in an evolutionary algorithm. This interface extends the [Operator](../../cl.ravenhill.keen.operators/-operator/index.md) interface, inheriting its core functionality while providing a semantic distinction that indicates the operator's primary role is to alter the genetic material.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the representation. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

## Functions

| Name | Summary |
|---|---|
| [invoke](../../cl.ravenhill.keen.operators/-operator/invoke.md) | [common]<br>abstract suspend operator fun &lt;[S](../../cl.ravenhill.keen.operators/-operator/invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](../../cl.ravenhill.keen.operators/-operator/invoke.md)&gt;&gt; [invoke](../../cl.ravenhill.keen.operators/-operator/invoke.md)(state: [S](../../cl.ravenhill.keen.operators/-operator/invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](../../cl.ravenhill.keen.operators/-operator/invoke.md)): Either&lt;[OperatorInvocationException](../../cl.ravenhill.keen.exceptions/-operator-invocation-exception/index.md), [S](../../cl.ravenhill.keen.operators/-operator/invoke.md)&gt;<br>Applies the operator to the given evolutionary state to produce a new state, supporting both synchronous and asynchronous execution. |
