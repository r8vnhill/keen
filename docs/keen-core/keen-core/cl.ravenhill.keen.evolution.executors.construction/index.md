//[keen-core](../../index.md)/[cl.ravenhill.keen.evolution.executors.construction](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ConstructorExecutor](-constructor-executor/index.md) | [common]<br>interface [ConstructorExecutor](-constructor-executor/index.md)&lt;[T](-constructor-executor/index.md)&gt;<br>Interface for constructing a list of elements either concurrently or sequentially. |
| [CoroutineConcurrentConstructor](-coroutine-concurrent-constructor/index.md) | [common]<br>class [CoroutineConcurrentConstructor](-coroutine-concurrent-constructor/index.md)&lt;[T](-coroutine-concurrent-constructor/index.md)&gt;(scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) : [ConstructorExecutor](-constructor-executor/index.md)&lt;[T](-coroutine-concurrent-constructor/index.md)&gt; <br>A concurrent constructor for generating sequences of values using Kotlin coroutines in an evolutionary algorithm. |
| [SequentialConstructor](-sequential-constructor/index.md) | [common]<br>class [SequentialConstructor](-sequential-constructor/index.md)&lt;[T](-sequential-constructor/index.md)&gt; : [ConstructorExecutor](-constructor-executor/index.md)&lt;[T](-sequential-constructor/index.md)&gt; <br>A sequential constructor for creating a list of elements in a specified order. |
