//[keen-genetics](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[transpose](transpose.md)

# transpose

[common]\
fun &lt;[E](transpose.md)&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[E](transpose.md)&gt;&gt;.[transpose](transpose.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[E](transpose.md)&gt;&gt;

Transposes a list of lists, effectively flipping rows and columns.

The `transpose` function transforms a list of lists (a matrix-like structure) such that rows become columns and columns become rows. This operation is useful in various contexts where matrix manipulation is required, such as in mathematical computations, data processing, or certain algorithmic transformations.

## Constraints:

- 
   **Uniformity**: The function requires that all inner lists (rows) must have the same size. If the lists do not have uniform sizes, an exception is thrown. If the list is empty or contains only one list, it is considered valid, and the result is the same as the original list.

#### Receiver

The list of lists (matrix) to be transposed.

#### Return

The transposed list of lists, where rows and columns are flipped.

#### Throws

| | |
|---|---|
| CompositeException | If the inner lists do not have the same size, an exception is thrown indicating the inconsistency. |
