//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[Box](index.md)/[fold](fold.md)

# fold

[common]\
open fun &lt;[U](fold.md)&gt; [fold](fold.md)(transform: ([T](index.md)) -&gt; [U](fold.md)): [U](fold.md)?

Applies the given transformation function to the value if it is present.

#### Return

The result of applying the function to the value, or `null` if no value is present.

#### Parameters

common

| | |
|---|---|
| U | The type of the result produced by the transformation function. |
| transform | The function to apply to the value. |
