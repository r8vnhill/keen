//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[Verifiable](index.md)

# Verifiable

interface [Verifiable](index.md)

Interface representing a verifiable component in the Keen evolutionary computation framework.

The `Verifiable` interface defines a contract for objects that can be verified for correctness or validity. Implementing classes can override the `verify` method to provide custom verification logic.

## Usage:

This interface is useful for ensuring that components within the evolutionary algorithm meet certain criteria or constraints before being used. The default implementation of `verify` returns `true`, indicating that the component is valid. Implementing classes can override this method to include specific verification logic.

### Example 1: Custom Verification

```kotlin
class MyFeature(
   val value: Int
) : Feature<Int, MyFeature>, Verifiable {
    override fun copyWithValue(value: Int) = MyFeature(value)
    override fun verify() = value 0
}
```

#### Inheritors

| |
|---|
| [Individual](../../cl.ravenhill.keen/-individual/index.md) |
| [Feature](../../cl.ravenhill.keen.repr/-feature/index.md) |
| [Representation](../../cl.ravenhill.keen.repr/-representation/index.md) |

## Functions

| Name | Summary |
|---|---|
| [verify](verify.md) | [common]<br>open fun [verify](verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the object. |
