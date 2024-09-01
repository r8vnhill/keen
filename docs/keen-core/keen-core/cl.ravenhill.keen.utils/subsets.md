//[keen-core](../../index.md)/[cl.ravenhill.keen.utils](index.md)/[subsets](subsets.md)

# subsets

[common]\
fun &lt;[T](subsets.md)&gt; [Random](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.random/-random/index.html).[subsets](subsets.md)(elements: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](subsets.md)&gt;, size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), exclusive: [Exclusivity](-exclusivity/index.md) = Exclusivity.NON_EXCLUSIVE, limit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = Int.MAX_VALUE): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](subsets.md)&gt;&gt;

Returns a list of subsets of a given size, where each subset contains a random selection of elements from the input list.

If [exclusive](subsets.md) is `true`, each element is only used once across all subsets. If `false`, an element can be used in multiple subsets.

The [size](subsets.md) parameter specifies the number of elements in each subset. It must be at least 1 and at most the size of the input list. If [exclusive](subsets.md) is `true`, the size of the input list must be a multiple of the subset size.

Each element in the input list is guaranteed to be included in at least one subset.

## Examples

### Generate three exclusive subsets of size two from a list of integers:

```kotlin
val elements = listOf(1, 2, 3, 4, 5, 6)
val size = 2
val exclusive = true
val limit = 3
val subsets = Random.subsets(elements, size, exclusive, limit)
// subsets: [[2, 6], [4, 3], [5, 1]]
```

### Generate four non-exclusive subsets of size three from a list of strings:

```kotlin
val elements = listOf("cat", "dog", "fish", "bird", "hamster")
val size = 3
val exclusive = false
val limit = 4
val subsets = Random.subsets(elements, size, exclusive, limit)
// subsets: [
//     ["hamster", "fish", "dog"], ["bird", "fish", "hamster"], ["cat", "dog", "fish"], ["hamster", "cat", "dog"]
// ]
```

### Generate two exclusive subsets of size four from a list of characters:

```kotlin
val elements = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l')
val size = 4
val exclusive = true
val limit = 2
val subsets = Random.subsets(elements, size, exclusive, limit)
// subsets: [["h", "j", "l", "f"], ["b", "g", "a", "e"]]
```

#### Return

a list of randomly generated subsets.

#### Parameters

common

| | |
|---|---|
| elements | the input list of elements to generate subsets from. |
| size | the size of each subset. |
| exclusive | whether each element can be used only once across all subsets. |
| limit | the maximum number of subsets to generate. Default is [Int.MAX_VALUE](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/-m-a-x_-v-a-l-u-e.html). |

#### Throws

| | |
|---|---|
| CompositeException | if the input parameters are invalid. |
