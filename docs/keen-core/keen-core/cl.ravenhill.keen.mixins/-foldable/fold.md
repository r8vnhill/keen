//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[Foldable](index.md)/[fold](fold.md)

# fold

[common]\
abstract fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)

Folds the elements of the structure from left to right, accumulating a result.

This method starts with an initial value and processes each element of the structure from left to right (i.e., in the order they appear), applying the given binary operation to the current accumulator and each element.

## Usage:

Folding to the left is typically used when the order of operations follows the structure's natural order. For example, summing gene values in a chromosome where the order of genes matters.

#### Return

The final accumulated result.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to the accumulator and each element of the structure. |
