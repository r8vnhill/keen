//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[Mappable](index.md)

# Mappable

interface [Mappable](index.md)&lt;[T](index.md)&gt;

Represents a type that can be transformed by applying a function to its contents.

The `Mappable` interface defines a contract for types that allow their contents to be transformed using a provided function. Implementing types must provide the ability to apply a transformation function to their internal value and return a new instance of `Mappable` containing the transformed value.

## Usage:

Implement this interface for any class or data structure that can apply a function to its contents and produce a new instance with the transformed value. This is particularly useful in scenarios where you want to apply transformations in a functional programming style, allowing the chaining of transformations.

#### Parameters

common

| | |
|---|---|
| T | The type of the value to be transformed. |

#### Inheritors

| |
|---|
| [Feature](../../cl.ravenhill.keen.repr/-feature/index.md) |
| [Representation](../../cl.ravenhill.keen.repr/-representation/index.md) |

## Functions

| Name | Summary |
|---|---|
| [map](map.md) | [common]<br>abstract fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Mappable](index.md)&lt;[T](index.md)&gt;<br>Applies the given transformation function to the value contained within the mappable structure. |
