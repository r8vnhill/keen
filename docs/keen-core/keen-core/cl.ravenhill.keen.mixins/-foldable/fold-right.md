//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[Foldable](index.md)/[foldRight](fold-right.md)

# foldRight

[common]\
abstract fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)

Folds the elements of the structure from right to left, accumulating a result.

This method starts with an initial value and processes each element of the structure from right to left (i.e., in reverse order), applying the given binary operation to the current element and the accumulator.

## Usage:

Folding to the right is useful when the order of operations should start from the end of the structure. For instance, in scenarios where the last element has more significance, or when building results in a right-associative manner, such as constructing a string representation of genes in reverse order.

#### Return

The final accumulated result.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to each element and the accumulator. |
