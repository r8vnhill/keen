//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[EvolutionState](index.md)/[foldRight](fold-right.md)

# foldRight

[common]\
open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)

Folds the values in the population from right to left, accumulating a result.

The `foldRight` function allows you to reduce the entire population to a single value by applying a binary operation to each element (i.e., each individual) and an initial value, processing elements from the last individual to the first. This is useful for operations where the order of processing should start from the end of the population and move towards the beginning, such as when building a result in reverse order.

## Efficiency Considerations:

- 
   **Folding Left (**`fold`**)**: Efficient for operations where accumulation naturally follows the sequence of individuals from first to last, such as summing values or combining results in the original order.
- 
   **Folding Right (**`foldRight`**)**: More efficient for operations where accumulation needs to start from the last individual and work towards the first, such as when constructing a result that depends on the order starting from the end.

#### Return

The final accumulated result after processing all individuals from right to left.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to each value in the population and the accumulator. |
